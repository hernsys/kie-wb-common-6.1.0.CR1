package org.kie.workbench.common.screens.home.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.security.authz.RuntimeResource;

import static java.util.Collections.*;

/**
 * A Section on the Home Page
 */
public class Section implements RuntimeResource {

    private final String heading;
    private final List<SectionEntry> entries = new ArrayList<SectionEntry>();
    private Collection<String> roles = new ArrayList<String>();

    public Section( final String heading ) {
        this.heading = PortablePreconditions.checkNotNull( "heading",
                                                           heading );
    }

    public String getHeading() {
        return heading;
    }

    public void addEntry( final SectionEntry entry ) {
        entries.add( PortablePreconditions.checkNotNull( "entry",
                                                         entry ) );
    }

    public List<SectionEntry> getEntries() {
        return Collections.unmodifiableList( entries );
    }

    public void setRoles(Collection<String> roles) {
        this.roles = PortablePreconditions.checkNotNull("roles", roles);
    }

    @Override
    public String getSignatureId() {
        return getClass().getName() + "#" + heading;
    }

    @Override
    public Collection<String> getRoles() {
        return roles;
    }

    @Override
    public Collection<String> getTraits() {
        return emptyList();
    }

}
