package org.kie.workbench.common.services.shared.rest;

import java.io.Serializable;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class JobResult implements Serializable {

    private JobStatus status;
    private String jobId;
    private String result;
    private Long lastModified;
    private List<String> detailedResult;

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus( JobStatus status ) {
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId( String jobId ) {
        this.jobId = jobId;
    }

    public String getResult() {
        return result;
    }

    public void setResult( String result ) {
        this.result = result;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified( long lastModified ) {
        this.lastModified = lastModified;
    }

    public List<String> getDetailedResult() {
        return detailedResult;
    }

    public void setDetailedResult( List<String> detailedResult ) {
        this.detailedResult = detailedResult;
    }
}
