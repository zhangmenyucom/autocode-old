<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${MAPPER_NAMESPACE}">
	<resultMap id="BaseResultMap" type="${MAPPER_RESULTMAP_TYPE}">
		<#list OBJ_PROPERTYS as pro>
			<#if 1 == pro.isPk>
		<id column="${pro.columnName}" property="${pro.propertyName}" jdbcType="${pro.columnTypeName}" />
			<#elseif 2004 == pro.columnType4SqlType || 2005 == pro.columnType4SqlType>
		<result column="${pro.columnName}" property="${pro.propertyName}" typeHandler="${MAPPER_BLOB_HANDLER_BEAN}"/>
			<#else>
		<result column="${pro.columnName}" property="${pro.propertyName}" jdbcType="${pro.columnTypeName}" />
			</#if>
	    </#list>
	</resultMap>
	
	<sql id="Base_Column_List">
		<#list OBJ_PROPERTYS as pro>
		${pro.columnName}<#if pro_index+1 < OBJ_PROPERTYS ? size>,</#if>
		</#list>
	</sql>
	
	<select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Integer">
		select
		<include refid="Base_Column_List" />
		from ${MAPPER_TABLE_NAME}
		<trim prefix="WHERE" prefixOverrides="AND|OR">
			<#list OBJ_PROPERTYS as pro>
				<#if 1 == pro.isPk>
					<#if 0 == pro_index>
		${pro.columnName} = ${pro.mapperArg}
					<#else>
		AND ${pro.columnName} = ${pro.mapperArg}
					</#if>
				</#if>
			</#list>
		</trim>
	</select>
	
	<delete id="deleteByPrimaryKey" parameterType="java.lang.Integer">
		delete from ${MAPPER_TABLE_NAME}
		<trim prefix="WHERE" prefixOverrides="AND|OR">
			<#list OBJ_PROPERTYS as pro>
				<#if 1 == pro.isPk>
					<#if 0 == pro_index>
		${pro.columnName} = ${pro.mapperArg}
					<#else>
		AND ${pro.columnName} = ${pro.mapperArg}
					</#if>
				</#if>
			</#list>
		</trim>
	</delete>
	
	<insert id="insertSelective" parameterType="${MAPPER_RESULTMAP_TYPE}">
		insert into ${MAPPER_TABLE_NAME}
		<trim prefix="(" suffix=")" suffixOverrides=",">
			<#list OBJ_PROPERTYS as pro>
			<if test="@${MAPPER_OGNL_BEAN}@isNotEmpty(${pro.propertyName})">
				${pro.columnName},
			</if>
			</#list>
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
			<#list OBJ_PROPERTYS as pro>
			<if test="@${MAPPER_OGNL_BEAN}@isNotEmpty(${pro.propertyName})">
				${pro.mapperArg},
			</if>
			</#list>
		</trim>
	</insert>
	
	<update id="updateByPrimaryKeySelective" parameterType="${MAPPER_RESULTMAP_TYPE}">
		update ${MAPPER_TABLE_NAME}
		<set>
			<#list OBJ_PROPERTYS as pro>
			<if test="@${MAPPER_OGNL_BEAN}@isNotEmpty(${pro.propertyName})">
				${pro.columnName} = ${pro.mapperArg},
			</if>
			</#list>
		</set>
		<trim prefix="WHERE" prefixOverrides="AND|OR">
			<#list OBJ_PROPERTYS as pro>
				<#if 1 == pro.isPk>
					<#if 0 == pro_index>
		${pro.columnName} = ${pro.mapperArg}
					<#else>
		AND ${pro.columnName} = ${pro.mapperArg}
					</#if>
				</#if>
			</#list>
		</trim>
	</update>
</mapper>