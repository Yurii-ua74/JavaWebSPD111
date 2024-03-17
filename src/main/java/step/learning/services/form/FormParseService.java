package step.learning.services.form;

import javax.servlet.http.HttpServletRequest;

public interface FormParseService {
    // request розкладається на файли та поля(форми)
    FormParseResult parse( HttpServletRequest request ) ;
}
