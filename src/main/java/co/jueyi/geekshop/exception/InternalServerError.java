package co.jueyi.geekshop.exception;

import graphql.ErrorClassification;
import graphql.ErrorType;

/**
 * This exception should be thrown when some unexpected and exceptional case is encountered.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class InternalServerError extends AbstractGraphqlException {
    public InternalServerError() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerError(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ExecutionAborted;
    }
}
