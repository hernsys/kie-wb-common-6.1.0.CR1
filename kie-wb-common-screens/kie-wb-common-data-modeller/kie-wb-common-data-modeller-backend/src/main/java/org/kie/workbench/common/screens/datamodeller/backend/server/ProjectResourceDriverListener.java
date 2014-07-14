/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.datamodeller.backend.server;

import org.guvnor.common.services.project.model.Project;
import org.guvnor.common.services.project.model.Package;
import org.guvnor.common.services.project.service.ProjectService;
import org.kie.workbench.common.services.datamodeller.driver.FileChangeDescriptor;
import org.kie.workbench.common.services.datamodeller.driver.ModelDriverListener;
import org.kie.workbench.common.services.datamodeller.util.FileHashingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.base.options.CommentedOption;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@RequestScoped
public class ProjectResourceDriverListener implements ModelDriverListener {

    private static final Logger logger = LoggerFactory.getLogger(ProjectResourceDriverListener.class);

    @Inject
    private ProjectService projectService;

    @Inject
    @Named("ioStrategy")
    IOService ioService;

    private CommentedOption option;

    private Project currentProject;

    private Package defaultPackage;

    List<FileChangeDescriptor> fileChanges = new ArrayList<FileChangeDescriptor>();

    public ProjectResourceDriverListener() {
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public void setOption(CommentedOption option) {
        this.option = option;
    }

    public void init() throws Exception {
        defaultPackage = projectService.resolveDefaultPackage(currentProject);
    }

    @Override
    public void assetGenerated( String fileName,
                                String content ) {

        String subDirName;
        org.uberfire.java.nio.file.Path subDirPath;
        org.uberfire.java.nio.file.Path destFilePath;
        StringTokenizer dirNames;
        Package subPackage;

        subDirPath = Paths.convert(defaultPackage.getPackageMainSrcPath());
        subPackage = defaultPackage;

        int index = fileName.lastIndexOf( "/" );
        if ( index == 0 ) {
            //the file names was provided in the form /SomeFile.java
            fileName = fileName.substring( 1, fileName.length() );
        } else if ( index > 0 ) {
            //the file name was provided in the most common form /dir1/dir2/SomeFile.java
            String dirNamesPath = fileName.substring( 0, index );
            fileName = fileName.substring( index + 1, fileName.length() );
            dirNames = new StringTokenizer( dirNamesPath, "/" );
            while ( dirNames.hasMoreElements() ) {
                subDirName = dirNames.nextToken();
                subDirPath = subDirPath.resolve( subDirName );
                if ( !ioService.exists( subDirPath ) ) {
                    //create the package using the projectService.
                    subPackage = projectService.newPackage(subPackage, subDirName);
                    //ioService.createDirectory( subDirPath );
                } else {
                    subPackage = projectService.resolvePackage( Paths.convert(subDirPath) );
                }
            }
        }

        //the last subDirPath is the directory to crate the file.
        destFilePath = subDirPath.resolve( fileName );
        boolean exists = ioService.exists( destFilePath );
        content = content != null ? content.trim() : null;
        String hashedContent = FileHashingUtils.setFileHashValue( content );

        ioService.write( destFilePath,
                hashedContent,
                option );

        if ( !exists ) {
            if ( logger.isDebugEnabled() ) {
                logger.debug( "Genertion listener created a new file: " + destFilePath );
            }
            fileChanges.add( new FileChangeDescriptor( destFilePath, FileChangeDescriptor.ADD ) );
        } else {
            if ( logger.isDebugEnabled() ) {
                logger.debug( "Generation listener modified file: " + destFilePath );
            }
            fileChanges.add( new FileChangeDescriptor( destFilePath, FileChangeDescriptor.UPDATE ) );
        }
    }

    public List<FileChangeDescriptor> getFileChanges() {
        return fileChanges;
    }
}