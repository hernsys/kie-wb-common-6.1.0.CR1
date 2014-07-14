/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.widgets.client.popups.file;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.kie.workbench.common.services.shared.validation.Validator;
import org.kie.workbench.common.services.shared.validation.ValidatorCallback;
import org.kie.workbench.common.widgets.client.resources.CommonImages;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.common.FormStylePopup;
import org.uberfire.workbench.type.ResourceTypeDefinition;

import static org.uberfire.commons.validation.PortablePreconditions.*;

public class CopyPopup extends FormStylePopup {

    final private TextBox nameTextBox = new TextBox();
    final private TextBox checkInCommentTextBox = new TextBox();

    public CopyPopup( final Path path,
                      final CommandWithFileNameAndCommitMessage command ) {
        this( path,
              new Validator() {
                  @Override
                  public void validate( final String value,
                                        final ValidatorCallback callback ) {
                      callback.onSuccess();
                  }
              },
              command );
    }

    public CopyPopup( final Path path,
                      final Validator validator,
                      final CommandWithFileNameAndCommitMessage command ) {
        super( CommonImages.INSTANCE.edit(),
               CommonConstants.INSTANCE.CopyPopupTitle() );

        checkNotNull( "validator",
                      validator );
        checkNotNull( "path",
                      path );
        checkNotNull( "command",
                      command );

        //Make sure it appears on top of other popups
        getElement().getStyle().setZIndex( Integer.MAX_VALUE );
        setGlassEnabled( true );

        nameTextBox.setTitle( CommonConstants.INSTANCE.NewName() );
        nameTextBox.setWidth( "200px" );
        addAttribute( CommonConstants.INSTANCE.NewNameColon(),
                      nameTextBox );

        checkInCommentTextBox.setTitle( CommonConstants.INSTANCE.CheckInComment() );
        checkInCommentTextBox.setWidth( "200px" );
        addAttribute( CommonConstants.INSTANCE.CheckInCommentColon(),
                      checkInCommentTextBox );

        final HorizontalPanel hp = new HorizontalPanel();
        final Button create = new Button( CommonConstants.INSTANCE.CopyPopupCreateACopy() );
        create.addClickHandler( new ClickHandler() {
            public void onClick( final ClickEvent arg0 ) {

                final String baseFileName = nameTextBox.getText();
                final String originalFileName = path.getFileName();
                final String extension = ( originalFileName.lastIndexOf( "." ) > 0 ? originalFileName.substring( originalFileName.lastIndexOf( "." ) ) : "" );
                final String fileName = baseFileName + extension;

                validator.validate( fileName,
                                    new ValidatorCallback() {
                                        @Override
                                        public void onSuccess() {
                                            hide();
                                            command.execute( new FileNameAndCommitMessage( baseFileName,
                                                                                           checkInCommentTextBox.getText() ) );
                                        }

                                        @Override
                                        public void onFailure() {
                                            Window.alert( CommonConstants.INSTANCE.InvalidFileName0( baseFileName ) );
                                        }
                                    } );

            }
        } );
        hp.add( create );

        final Button cancel = new Button( CommonConstants.INSTANCE.Cancel() );
        cancel.addClickHandler( new ClickHandler() {
            public void onClick( final ClickEvent arg0 ) {
                hide();
            }
        } );
        hp.add( new HTML( "&nbsp" ) );
        hp.add( cancel );
        addAttribute( "", hp );

    }

    private String buildFileName( final String baseFileName,
                                  final ResourceTypeDefinition resourceType ) {
        final String suffix = resourceType.getSuffix();
        final String prefix = resourceType.getPrefix();
        final String extension = !( suffix == null || "".equals( suffix ) ) ? "." + resourceType.getSuffix() : "";
        if ( baseFileName.endsWith( extension ) ) {
            return prefix + baseFileName;
        }
        return prefix + baseFileName + extension;
    }

}
