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
package org.kie.workbench.common.widgets.decoratedgrid.client.widget.cells;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.util.Date;

/**
 * A Popup Date Editor.
 */
public class PopupDateEditCell extends AbstractPopupEditCell<Date, Date> {

    private final DatePicker     datePicker;
    private final DateTimeFormat format;

    public PopupDateEditCell( DateTimeFormat format,
                              boolean isReadOnly ) {
        super( isReadOnly );
        if ( format == null ) {
            throw new IllegalArgumentException( "format == null" );
        }

        this.format = format;
        this.datePicker = new DatePicker();

        // Hide the panel and call valueUpdater.update when a date is selected
        datePicker.addValueChangeHandler( new ValueChangeHandler<Date>() {

            public void onValueChange( ValueChangeEvent<Date> event ) {

                // Update value
                Date date = event.getValue();
                setValue( lastContext,
                          lastParent,
                          date );
                if ( valueUpdater != null ) {
                    valueUpdater.update( date );
                }

                panel.hide();
            }
        } );

        vPanel.add( datePicker );
    }

    @Override
    public void render( Context context,
                        Date value,
                        SafeHtmlBuilder sb ) {
        if ( value != null ) {
            sb.append( renderer.render( format.format( value ) ) );
        }
    }

    // Commit the change
    @Override
    protected void commit() {

        // Update value
        Date date = datePicker.getValue();
        setValue( lastContext,
                  lastParent,
                  date );
        if ( valueUpdater != null ) {
            valueUpdater.update( date );
        }
        panel.hide();
    }

    // Start editing the cell
    @Override
    @SuppressWarnings("deprecation")
    protected void startEditing( final Context context,
                                 final Element parent,
                                 final Date value ) {

        // Default date
        Date date = value;
        if ( value == null ) {
            Date d = new Date();
            int year = d.getYear();
            int month = d.getMonth();
            int dom = d.getDate();
            date = new Date( year,
                             month,
                             dom );
        }
        datePicker.setCurrentMonth( date );
        datePicker.setValue( date );

        panel.setPopupPositionAndShow( new PositionCallback() {
            public void setPosition( int offsetWidth,
                                     int offsetHeight ) {
                panel.setPopupPosition( parent.getAbsoluteLeft()
                                                + offsetX,
                                        parent.getAbsoluteTop()
                                                + offsetY );
            }
        } );

    }

}
