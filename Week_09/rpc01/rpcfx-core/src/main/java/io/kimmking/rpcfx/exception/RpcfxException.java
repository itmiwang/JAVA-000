package io.kimmking.rpcfx.exception;

import okhttp3.internal.http2.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static okhttp3.internal.http2.ErrorCode.PROTOCOL_ERROR;


/**
 * @author guozq
 * @date 2020-12-18-1:54 上午
 */
public class RpcfxException extends RuntimeException {
    private static final Logger LOG = LoggerFactory.getLogger(RpcfxException.class);
    private static final long serialVersionUID = 327492663739720650L;
    
    /** 错误码 */
    protected final ErrorCode errorCode;
    //    NO_ERROR(0),
    //    PROTOCOL_ERROR(1),
    //    INTERNAL_ERROR(2),
    //    FLOW_CONTROL_ERROR(3),
    //    REFUSED_STREAM(7),
    //    CANCEL(8),
    //    COMPRESSION_ERROR(9),
    //    CONNECT_ERROR(10),
    //    ENHANCE_YOUR_CALM(11),
    //    INADEQUATE_SECURITY(12),
    //    HTTP_1_1_REQUIRED(13);
    
    /**
     * 这个是和谐一些不必要的地方,冗余的字段
     * 尽量不要用
     */
    private String code;
    
    /**
     * 无参默认构造UNSPECIFIED
     */
    public RpcfxException() {
        super("");
        this.errorCode = PROTOCOL_ERROR;
    }
    
    /**
     * 指定错误码构造通用异常
     * @param errorCode 错误码
     */
    public RpcfxException(final ErrorCode errorCode) {
        super("");
        this.errorCode = errorCode;
    }
    
    /**
     * 构造通用异常
     * @param errorCode 错误码
     * @param detailedMessage 详细描述
     * @param t 导火索
     */
    public RpcfxException(final ErrorCode errorCode, final String detailedMessage,
                          final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = errorCode;
    }
    
    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return property value of errorCode
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
}
