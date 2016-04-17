package ${OBJ_DAO_PACKAGE};

import org.springframework.stereotype.Component;

<#list OBJ_IMPORTS as imp>  
import ${imp};  
</#list>

/**
 * @notes:${OBJ_NOTES}
 *
 * @author ${OBJ_AUTHOR}
 *
 * @date   ${OBJ_CDATE}	Email:${OBJ_AUTHOR_EMAIL}
 */
@Component
public interface ${OBJ_NAME} extends BaseDao<${OBJ_T_BEAN}> {

}
