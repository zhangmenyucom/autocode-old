package ${OBJ_SERVICE_IMPL_PACKAGE};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class ${OBJ_NAME} extends BaseServiceImpl<${OBJ_T_BEAN}> implements ${OBJ_IMPL_BEAN} {

	@Autowired
	private ${OBJ_DAO_NAME} ${OBJ_DAO_NAME_PRO};
	
	@Autowired
	public ${OBJ_DAO_NAME} getThisDao() {
		return ${OBJ_DAO_NAME_PRO};
	}	
}
