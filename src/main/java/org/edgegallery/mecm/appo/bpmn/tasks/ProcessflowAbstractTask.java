package org.edgegallery.mecm.appo.bpmn.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public abstract class ProcessflowAbstractTask {

    public static final String RESPONSE = "Response";
    public static final String RESPONSE_CODE = "ResponseCode";
    public static final String ERROR_RESPONSE = "ErrResponse";
    public static final String FLOW_EXCEPTION = "ProcessflowException";

    /**
     * Retrieves protocol.
     *
     * @return protocol
     */
    public String getProtocol(String isSslEnabled) {
        if (isSslEnabled.equals("true")) {
            return "https://";
        }
        return "http://";
    }

    /**
     * Sets process flow response attributes to delegate execution.
     *
     * @param delegateExecution delegate execution
     * @param response          response
     * @param responseCode      response code
     */
    public void setProcessflowResponseAttributes(DelegateExecution delegateExecution,
                                                 String response, String responseCode) {
        if (responseCode == null) {
            throw new IllegalArgumentException();
        }
        delegateExecution.setVariable(RESPONSE, response);
        delegateExecution.setVariable(RESPONSE_CODE, responseCode);
    }

    /**
     * Sets process flow error response attributes to delegate execution.
     *
     * @param delegateExecution delegate execution
     * @param response          response
     * @param responseCode      response code
     */
    public void setProcessflowErrorResponseAttributes(DelegateExecution delegateExecution,
                                                      String response, String responseCode) {
        if (responseCode == null) {
            throw new IllegalArgumentException();
        }
        delegateExecution.setVariable(ERROR_RESPONSE, response);
        delegateExecution.setVariable(RESPONSE_CODE, responseCode);
    }

    /**
     * Sets process flow exception response attributes to delegate execution.
     *
     * @param delegateExecution delegate execution
     * @param response          response
     * @param responseCode      response code
     */
    public void setProcessflowExceptionResponseAttributes(DelegateExecution delegateExecution,
                                                          String response, String responseCode) {
        if (responseCode == null) {
            throw new IllegalArgumentException();
        }
        delegateExecution.setVariable(RESPONSE_CODE, responseCode);
        delegateExecution.setVariable(FLOW_EXCEPTION, response);
    }
}
