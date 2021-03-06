package org.kie.workbench.common.screens.defaulteditor.client.editor;

import javax.enterprise.context.ApplicationScoped;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import org.kie.workbench.common.screens.defaulteditor.service.XmlResourceTypeDefinition;
import org.uberfire.client.resources.CoreImages;
import org.uberfire.client.resources.i18n.CoreConstants;
import org.uberfire.client.workbench.type.ClientResourceType;

@ApplicationScoped
public class XmlResourceType extends XmlResourceTypeDefinition implements ClientResourceType {

    private static final Image IMAGE = new Image( CoreImages.INSTANCE.file());

    @Override
    public IsWidget getIcon() {
        return IMAGE;
    }

    @Override
    public String getDescription() {
        String desc = CoreConstants.INSTANCE.textResourceTypeDescription();
        if ( desc == null || desc.isEmpty() ) return super.getDescription();
        return desc;
    }

}
