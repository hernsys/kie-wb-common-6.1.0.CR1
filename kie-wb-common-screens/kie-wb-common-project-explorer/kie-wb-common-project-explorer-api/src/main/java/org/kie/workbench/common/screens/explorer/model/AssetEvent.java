package org.kie.workbench.common.screens.explorer.model;



import org.jboss.errai.common.client.api.annotations.Portable;
/**
 * 
 * @author hernsys
 *
 */
@Portable
public class AssetEvent {

    private String url;

    public AssetEvent() {

    }
    
    public AssetEvent(String url) {
    	this.url = url;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}

