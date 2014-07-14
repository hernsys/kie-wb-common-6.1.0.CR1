package org.kie.workbench.common.services.backend.session;

import org.guvnor.common.services.project.model.Project;
import org.kie.api.runtime.KieSession;

/**
 * Provides a KSession for given project
 */
public interface SessionService {

    /**
     * Returns the default KSession for a Project
     */
    KieSession newKieSession( final Project project );

}
