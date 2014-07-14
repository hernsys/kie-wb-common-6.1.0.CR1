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

package org.kie.workbench.common.widgets.client.popups.list;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.commons.data.Pair;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.uberfire.client.common.Popup;
import org.uberfire.client.common.popups.errors.ErrorPopup;

public class FormListPopupViewImpl
        extends Popup
        implements FormListPopupView {

    private final Widget widget;
    private Presenter presenter;

    interface AddNewKBasePopupViewImplBinder
            extends
            UiBinder<Widget, FormListPopupViewImpl> {

    }

    private static AddNewKBasePopupViewImplBinder uiBinder = GWT.create( AddNewKBasePopupViewImplBinder.class );

    @UiField
    ListBox listItems;

    @UiField
    Button okButton;

    @UiField
    Button cancelButton;

    public FormListPopupViewImpl() {
        widget = uiBinder.createAndBindUi( this );
        setTitle( CommonConstants.INSTANCE.New() );
    }

    @Override
    public void setPresenter( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public Widget getContent() {
        return widget;
    }

    @Override
    public void setItems( final List<Pair<String, String>> items ) {
        listItems.clear();
        for ( Pair<String, String> item : items ) {
            listItems.addItem( item.getK1(),
                               item.getK2() );
        }
    }

    @Override
    public Pair<String, String> getSelectedItem() {
        final int selectedIndex = listItems.getSelectedIndex();
        if ( selectedIndex == -1 ) {
            return Pair.newPair( "", "" );
        }
        final String text = listItems.getItemText( selectedIndex );
        final String value = listItems.getValue( selectedIndex );
        return Pair.newPair( text, value );
    }

    @Override
    public void showFieldEmptyWarning() {
        ErrorPopup.showMessage( CommonConstants.INSTANCE.PleaseSetAName() );
    }

    @UiHandler("okButton")
    public void ok( ClickEvent clickEvent ) {
        presenter.onOk();
        hide();
    }

    @UiHandler("cancelButton")
    public void cancel( ClickEvent clickEvent ) {
        hide();
    }

}
