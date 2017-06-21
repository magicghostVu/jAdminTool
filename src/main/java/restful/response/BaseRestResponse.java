package restful.response;

/**
 * Created by Fresher on 21/06/2017.
 */
public class BaseRestResponse {
    private RestfulErrorDefine error;
    private String content;
    public BaseRestResponse(RestfulErrorDefine error, String content) {
        this.error = error;
        this.content = content;
    }
}
