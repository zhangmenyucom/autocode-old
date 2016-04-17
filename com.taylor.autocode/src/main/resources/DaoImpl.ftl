package ${OBJ_DAO_IMPL_PACKAGE};

import org.springframework.stereotype.Repository;

<#list OBJ_IMPORTS as imp>  
import ${imp};  
</#list>

/**
 * @notes:${OBJ_NOTES}
 *
 * @author ${OBJ_AUTHOR}
 *
 * @date ${OBJ_CDATE}	Email:${OBJ_AUTHOR_EMAIL}
 */
@Repository
public class ${OBJ_NAME} extends BaseDaoImpl<${OBJ_T_BEAN}> implements ${OBJ_IMPL_BEAN} {

	@Override
	public String getDao4MapperPackage() {
		return "${MAPPER_NAMESPACE}";
	}

}