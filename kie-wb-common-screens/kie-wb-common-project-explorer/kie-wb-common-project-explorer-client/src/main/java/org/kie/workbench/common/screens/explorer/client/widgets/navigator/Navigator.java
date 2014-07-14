package org.kie.workbench.common.screens.explorer.client.widgets.navigator;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;
import org.kie.workbench.common.screens.explorer.client.widgets.ViewPresenter;
import org.kie.workbench.common.screens.explorer.model.FolderItem;
import org.kie.workbench.common.screens.explorer.model.FolderListing;

public interface Navigator extends IsWidget {

    void setOptions( final NavigatorOptions options );

    void loadContent( final FolderListing content );

    void loadContent( final FolderListing content,
                      final Map<FolderItem, List<FolderItem>> siblings );

    boolean isAttached();

    void clear();

    void setPresenter( final ViewPresenter presenter );

    public static interface NavigatorItem {

        public void addDirectory( final FolderItem child );

        public void addFile( final FolderItem child );

        void cleanup();
    }

}
