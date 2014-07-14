package org.kie.workbench.common.widgets.client.datamodel;

import org.drools.workbench.models.datamodel.imports.Imports;
import org.drools.workbench.models.datamodel.oracle.PackageDataModelOracle;
import org.guvnor.common.services.project.model.Package;
import org.guvnor.common.services.project.model.Project;
import org.guvnor.common.services.project.service.ProjectService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.services.datamodel.backend.server.IncrementalDataModelServiceImpl;
import org.kie.workbench.common.services.datamodel.backend.server.cache.LRUDataModelOracleCache;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleIncrementalPayload;
import org.kie.workbench.common.services.datamodel.service.IncrementalDataModelService;
import org.uberfire.backend.vfs.Path;

import static org.mockito.Mockito.*;

public class MockIncrementalDataModelServiceCaller implements Caller<IncrementalDataModelService> {

    private IncrementalDataModelServiceImplWrapper service;

    public MockIncrementalDataModelServiceCaller() {
        this( mock( PackageDataModelOracle.class ) );
    }

    public MockIncrementalDataModelServiceCaller( final PackageDataModelOracle packageLoader ) {
        final Project project = mock( Project.class );
        final Package pkg = new Package( mock( Path.class ),
                                         mock( Path.class ),
                                         mock( Path.class ),
                                         mock( Path.class ),
                                         mock( Path.class ),
                                         packageLoader.getPackageName(),
                                         packageLoader.getPackageName(),
                                         packageLoader.getPackageName() );
        final LRUDataModelOracleCache cachePackages = mock( LRUDataModelOracleCache.class );
        when( cachePackages.assertPackageDataModelOracle( project,
                                                          pkg ) ).thenReturn( packageLoader );

        final ProjectService projectService = mock( ProjectService.class );
        when( projectService.resolveProject( any( Path.class ) ) ).thenReturn( project );
        when( projectService.resolvePackage( any( Path.class ) ) ).thenReturn( pkg );

        this.service = new IncrementalDataModelServiceImplWrapper( cachePackages,
                                                                   projectService );
    }

    @Override
    public IncrementalDataModelService call() {
        return service;
    }

    @Override
    public IncrementalDataModelService call( final RemoteCallback<?> remoteCallback ) {
        service.setCallback( remoteCallback );
        return service;
    }

    @Override
    public IncrementalDataModelService call( final RemoteCallback<?> remoteCallback,
                                             final ErrorCallback<?> errorCallback ) {
        service.setCallback( remoteCallback );
        return service;
    }

    private static class IncrementalDataModelServiceImplWrapper extends IncrementalDataModelServiceImpl {

        private RemoteCallback<?> remoteCallback;

        public IncrementalDataModelServiceImplWrapper( final LRUDataModelOracleCache cachePackages,
                                                       final ProjectService projectService ) {
            super( cachePackages,
                   projectService );
        }

        public void setCallback( final RemoteCallback<?> remoteCallback ) {
            this.remoteCallback = remoteCallback;
        }

        @Override
        public PackageDataModelOracleIncrementalPayload getUpdates( final Path resourcePath,
                                                                    final Imports imports,
                                                                    final String factType ) {
            final PackageDataModelOracleIncrementalPayload payload = super.getUpdates( resourcePath,
                                                                                       imports,
                                                                                       factType );
            final RemoteCallback r = remoteCallback;
            r.callback( payload );
            return payload;
        }

    }

}
