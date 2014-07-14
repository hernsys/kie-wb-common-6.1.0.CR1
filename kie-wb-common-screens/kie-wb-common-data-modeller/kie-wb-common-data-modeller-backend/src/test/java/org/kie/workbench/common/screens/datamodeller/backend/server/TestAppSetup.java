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

import java.util.HashSet;
import java.util.Map;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

import org.apache.lucene.analysis.Analyzer;
import org.guvnor.common.services.project.service.KModuleService;
import org.guvnor.m2repo.service.M2RepoService;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.uberfire.backend.server.config.ConfigurationService;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.metadata.backend.lucene.LuceneConfig;
import org.uberfire.metadata.backend.lucene.LuceneConfigBuilder;
import org.uberfire.metadata.engine.Indexer;
import org.uberfire.metadata.io.IOServiceIndexedImpl;
import org.uberfire.rpc.SessionInfo;

import static org.mockito.Mockito.*;

@Singleton
@Alternative
public class TestAppSetup {

    private final IOService ioService = new IOServiceDotFileImpl();

    @Produces
    @Named("ioStrategy")
    public IOService ioService() {
        return ioService;
    }

    @Produces
    @Alternative
    public M2RepoService m2RepoService() {
        return mock( M2RepoService.class );
    }

    @Produces
    @Alternative
    public KModuleService kModuleService() {
        return mock( KModuleService.class );
    }

    @Produces
    @Alternative
    public ConfigurationService configurationService() {
        return mock( ConfigurationService.class );
    }

    @Produces
    @Alternative
    public SessionInfo sessionInfo() {
        return mock( SessionInfo.class );
    }

    @Produces
    @Named("uf")
    public ServletContext servletContext() {
        return mock( ServletContext.class );
    }

    @Produces
    @Named("luceneConfig")
    public LuceneConfig luceneConfig() {
        return mock( LuceneConfig.class );
    }
    /*
    protected IOService ioService() {
        if ( ioService == null ) {
            final TestIndexer indexer = getIndexer();
            final Map<String, Analyzer> analyzers = getAnalyzers();
            config = new LuceneConfigBuilder()
                    .withInMemoryMetaModelStore()
                    .usingIndexers( new HashSet<Indexer>() {{
                        add( indexer );
                    }} )
                    .usingAnalyzers( analyzers )
                    .useDirectoryBasedIndex()
                    .useInMemoryDirectory()
                    .build();

            //Mock CDI injection and setup
            ioService = new IOServiceIndexedImpl( config.getIndexEngine(),
                    config.getIndexers() );
            indexer.setIOService( ioService );
            indexer.setResourceTypeDefinition( getResourceTypeDefinition() );
        }
        return ioService;
    }
    */

}
