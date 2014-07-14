package org.kie.workbench.common.widgets.client.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.core.client.Callback;
import org.guvnor.common.services.project.context.ProjectContext;
import org.guvnor.common.services.project.context.ProjectContextChangeEvent;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;

/**
 * A menu to create New Resources
 */
@ApplicationScoped
public class NewResourcesMenu {

    @Inject
    private SyncBeanManager iocBeanManager;

    @Inject
    private NewResourcePresenter newResourcePresenter;

    private final List<MenuItem> items = new ArrayList<MenuItem>();
    private final Map<NewResourceHandler, MenuItem> newResourceHandlers = new HashMap<NewResourceHandler, MenuItem>();

    @PostConstruct
    public void setup() {
        final Collection<IOCBeanDef<NewResourceHandler>> handlerBeans = iocBeanManager.lookupBeans( NewResourceHandler.class );
        if ( handlerBeans.size() > 0 ) {
            for ( IOCBeanDef<NewResourceHandler> handlerBean : handlerBeans ) {
                final NewResourceHandler activeHandler = handlerBean.getInstance();
                final String description = activeHandler.getDescription();
                final MenuItem menuItem = MenuFactory.newSimpleItem( description ).respondsWith( new Command() {
                    @Override
                    public void execute() {
                        newResourcePresenter.show( activeHandler );
                    }
                } ).endMenu().build().getItems().get( 0 );
                newResourceHandlers.put( activeHandler,
                                         menuItem );
                items.add( menuItem );
            }
        }

        //Sort MenuItems by caption
        Collections.sort( items,
                          new Comparator<MenuItem>() {
                              @Override
                              public int compare( final MenuItem o1,
                                                  final MenuItem o2 ) {
                                  return o1.getCaption().compareToIgnoreCase( o2.getCaption() );
                              }
                          } );

    }

    public List<MenuItem> getMenuItems() {
        return items;
    }

    public void onProjectContextChanged( @Observes final ProjectContextChangeEvent event ) {
        final ProjectContext context = new ProjectContext();
        context.setActiveOrganizationalUnit( event.getOrganizationalUnit() );
        context.setActiveRepository( event.getRepository() );
        context.setActiveProject( event.getProject() );
        context.setActivePackage( event.getPackage() );
        enableNewResourceHandlers( context );
    }

    private void enableNewResourceHandlers( final ProjectContext context ) {
        for ( Map.Entry<NewResourceHandler, MenuItem> e : this.newResourceHandlers.entrySet() ) {
            final NewResourceHandler handler = e.getKey();
            final MenuItem menuItem = e.getValue();
            handler.acceptContext( context,
                                   new Callback<Boolean, Void>() {
                                       @Override
                                       public void onFailure( Void reason ) {
                                           // Nothing to do there right now.
                                       }

                                       @Override
                                       public void onSuccess( final Boolean result ) {
                                           if ( result != null ) {
                                               menuItem.setEnabled( result );
                                           }
                                       }
                                   } );

        }
    }

}
