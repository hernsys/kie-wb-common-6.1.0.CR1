package org.kie.workbench.common.widgets.client.datamodel;

import java.util.List;
import java.util.Set;

import org.drools.workbench.models.datamodel.imports.HasImports;
import org.drools.workbench.models.datamodel.oracle.MethodInfo;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.kie.workbench.common.services.datamodel.model.PackageDataModelOracleBaselinePayload;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;

/**
 * Utility methods for AsyncPackageDataModelOracle tests
 */
public class PackageDataModelOracleTestUtils {

    public static void assertContains( final String string,
                                       final String[] strings ) {
        for ( int i = 0; i < strings.length; i++ ) {
            if ( string.equals( strings[ i ] ) ) {
                return;
            }
        }
        fail( "String[] did not contain: " + string );
    }

    public static void assertContains( final String string,
                                       final Set<String> strings ) {
        if ( !strings.contains( string ) ) {
            fail( "Set<String> did not contain: " + string );
        }
    }

    public static void assertContains( final String fieldName,
                                       final ModelField[] fieldDefinitions ) {
        for ( int i = 0; i < fieldDefinitions.length; i++ ) {
            if ( fieldName.equals( fieldDefinitions[ i ].getName() ) ) {
                return;
            }
        }
        fail( "ModelField[] did not contain field: " + fieldName );
    }

    public static void assertContains( final String methodName,
                                       final List<MethodInfo> methodInfos ) {
        for ( int i = 0; i < methodInfos.size(); i++ ) {
            if ( methodName.equals( methodInfos.get( i ).getName() ) ) {
                return;
            }
        }
        fail( "List<MethodInfo> did not contain field: " + methodName );
    }

    public static void populateDataModelOracle( final Path resourcePath,
                                                final HasImports hasImports,
                                                final AsyncPackageDataModelOracle oracle,
                                                final PackageDataModelOracleBaselinePayload payload ) {
        populate( oracle,
                  payload );
        oracle.init( resourcePath );
        oracle.filter( hasImports.getImports() );
    }

    private static void populate( final AsyncPackageDataModelOracle oracle,
                                  final PackageDataModelOracleBaselinePayload payload ) {
        oracle.setProjectName( payload.getProjectName() );
        oracle.addModelFields( payload.getModelFields() );
        oracle.addFieldParametersType( payload.getFieldParametersType() );
        oracle.addEventTypes( payload.getEventTypes() );
        oracle.addTypeSources( payload.getTypeSources() );
        oracle.addSuperTypes( payload.getSuperTypes() );
        oracle.addTypeAnnotations( payload.getTypeAnnotations() );
        oracle.addTypeFieldsAnnotations( payload.getTypeFieldsAnnotations() );
        oracle.addJavaEnumDefinitions( payload.getJavaEnumDefinitions() );
        oracle.addMethodInformation( payload.getMethodInformation() );
        oracle.addCollectionTypes( payload.getCollectionTypes() );
        oracle.addPackageNames( payload.getPackageNames() );

        oracle.setPackageName( payload.getPackageName() );
        oracle.addWorkbenchEnumDefinitions( payload.getWorkbenchEnumDefinitions() );
        oracle.addDslConditionSentences( payload.getDslConditionSentences() );
        oracle.addDslActionSentences( payload.getDslActionSentences() );
        oracle.addGlobals( payload.getGlobals() );
    }

}
