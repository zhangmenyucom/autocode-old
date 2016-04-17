package ${OBJ_SERVICE_PACKAGE};

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
public interface ${OBJ_NAME} extends BaseService<${OBJ_T_BEAN}> {

}
