package com.j256.ormlite.field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.types.VoidType;

/**
 * Annotation that identifies a field in a class that corresponds to a column in the database and will be persisted.
 * Fields that are not to be persisted such as transient or other temporary fields probably should be ignored. For
 * example:
 * 
 * <p>
 * <blockquote>
 * 
 * <pre>
 * &#064;DatabaseField(id = true)
 * private String name;
 * 
 * &#064;DatabaseField(columnName = &quot;passwd&quot;, canBeNull = false)
 * private String password;
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * <p>
 * <b> WARNING:</b> If you add any extra fields here, you will need to add them to {@link DatabaseFieldConfig} as well.
 * </p>
 * 
 * @author graywatson
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface DatabaseField {

	/** this special string is used as a .equals check to see if no default was specified */
	public static final String DEFAULT_STRING = "__ormlite__ no default value string was specified";

	/**
	 * Default for the maxForeignAutoRefreshLevel.
	 * 
	 * @see #maxForeignAutoRefreshLevel()
	 */
	public static final int DEFAULT_MAX_FOREIGN_AUTO_REFRESH_LEVEL = 2;

	/**
	 * The name of the column in the database. If not set then the name is taken from the field name.
	 */
	String columnName() default "";

	/**
	 * The DataType associated with the field. If not set then the Java class of the field is used to match with the
	 * appropriate DataType.
	 */
	DataType dataType() default DataType.UNKNOWN;

	/**
	 * The default value of the field for creating the table. Default is none.
	 */
	String defaultValue() default DEFAULT_STRING;

	/**
	 * Width of array fields (often for strings). Default is 0 which means to take the data-type and database-specific
	 * default. For strings that means 255 characters although some databases do not support this.
	 */
	int width() default 0;

	/**
	 * Whether the field can be assigned to null or have no value. Default is true.
	 */
	boolean canBeNull() default true;

	/**
	 * Whether the field is the id field or not. Default is false. Only one field can have this set in a class. If you
	 * don't have it set then you won't be able to use the query, update, and delete by ID methods. Only one of this,
	 * {@link #generatedId}, and {@link #generatedIdSequence} can be specified.
	 */
	boolean id() default false;

	/**
	 * Whether the field is an auto-generated id field. Default is false. With databases for which
	 * {@link DatabaseType#isIdSequenceNeeded} is true then this will cause the name of the sequence to be
	 * auto-generated. To specify the name of the sequence use {@link #generatedIdSequence}. Only one of this,
	 * {@link #id}, and {@link #generatedIdSequence} can be specified.
	 */
	boolean generatedId() default false;

	/**
	 * The name of the sequence number to be used to generate this value. Default is none. This is only necessary for
	 * database for which {@link DatabaseType#isIdSequenceNeeded} is true and you already have a defined sequence that
	 * you want to use. If you use {@link #generatedId} instead then the code will auto-generate a sequence name. Only
	 * one of this, {@link #id}, and {@link #generatedId} can be specified.
	 */
	String generatedIdSequence() default "";

	/**
	 * Field is a non-primitive object that corresponds to another class that is also stored in the database. It must
	 * have an id field (either {@link #id}, {@link #generatedId}, or {@link #generatedIdSequence} which will be stored
	 * in this table. When an object is returned from a query call, any foreign objects will just have the id field set
	 * in it. To get all of the other fields you will have to do a refresh on the object using its own Dao.
	 */
	boolean foreign() default false;

	/**
	 * Package should use get...() and set...() to access the field value instead of the default direct field access via
	 * reflection. This may be necessary if the object you are storing has protections around it.
	 * 
	 * <p>
	 * <b>NOTE:</b> The name of the get method <i>must</i> match getXxx() where Xxx is the name of the field with the
	 * first letter capitalized. The get <i>must</i> return a class which matches the field's. The set method
	 * <i>must</i> match setXxx(), have a single argument whose class matches the field's, and return void. For example:
	 * </p>
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	 * &#064;DatabaseField
	 * private Integer orderCount;
	 * 
	 * public Integer getOrderCount() {
	 * 	return orderCount;
	 * }
	 * 
	 * public void setOrderCount(Integer orderCount) {
	 * 	this.orderCount = orderCount;
	 * }
	 * </pre>
	 * 
	 * </blockquote>
	 */
	boolean useGetSet() default false;

	/**
	 * If the field is an Enum and the database has a value that is not one of the names in the enum then this name will
	 * be used instead. It must match one of the enum names. This is mainly useful when you are worried about backwards
	 * compatibility with older database rows or future compatibility if you have to roll back to older data definition.
	 */
	String unknownEnumName() default "";

	/**
	 * If this is set to true (default false) then it will throw a SQLException if a null value is attempted to be
	 * de-persisted into a primitive. This must only be used on a primitive field. If this is false then if the database
	 * field is null, the value of the primitive will be set to 0.
	 */
	boolean throwIfNull() default false;

	/**
	 * Set this to be false (default true) to not store this field in the database. This is useful if you want to have
	 * the annotation on all of your fields but turn off the writing of some of them to the database.
	 */
	boolean persisted() default true;

	/**
	 * Optional format information that can be used by various field types. For example, if the Date is to be persisted
	 * as a string, this can set what format string to use for the date.
	 */
	String format() default "";

	/**
	 * Set this to be true (default false) to have the database insure that the column is unique to all rows in the
	 * table. Use this when you wan a field to be unique even if it is not the identify field. For example, if you have
	 * the firstName and lastName fields, both with unique=true and you have "Bob", "Smith" in the database, you cannot
	 * insert either "Bob", "Jones" or "Kevin", "Smith".
	 */
	boolean unique() default false;

	/**
	 * Set this to be true (default false) to have the database insure that _all_ of the columns marked with this as
	 * true will together be unique. For example, if you have the firstName and lastName fields, both with unique=true
	 * and you have "Bob", "Smith" in the database, you cannot insert another "Bob", "Smith" but you can insert "Bob",
	 * "Jones" and "Kevin", "Smith".
	 */
	boolean uniqueCombo() default false;

	/**
	 * Set this to be true (default false) to have the database add an index for this field. This will create an index
	 * with the name columnName + "_idx". To specify a specific name of the index or to index multiple fields, use
	 * {@link #indexName()}.
	 */
	boolean index() default false;

	/**
	 * Set this to be true (default false) to have the database add a unique index for this field. This is the same as
	 * the {@link #index()} field but this ensures that all of the values in the index are unique..
	 */
	boolean uniqueIndex() default false;

	/**
	 * Set this to be a string (default none) to have the database add an index for this field with this name. You do
	 * not need to specify the {@link #index()} boolean as well. To index multiple fields together in one index, each of
	 * the fields should have the same indexName value.
	 */
	String indexName() default "";

	/**
	 * Set this to be a string (default none) to have the database add a unique index for this field with this name.
	 * This is the same as the {@link #indexName()} field but this ensures that all of the values in the index are
	 * unique.
	 */
	String uniqueIndexName() default "";

	/**
	 * Set this to be true (default false) to have a foreign field automagically refreshed when an object is queried.
	 * This will _not_ automagically create the foreign object but when the object is queried, a separate database call
	 * will be made to load of the fields of the foreign object via an internal DAO. The default is to just have the ID
	 * field in the object retrieved and for the caller to call refresh on the correct DAO.
	 * 
	 * <p>
	 * <b>NOTE:</b> This will create another DAO object so that low memory devices may want to call refresh by hand.
	 * </p>
	 */
	boolean foreignAutoRefresh() default false;

	/**
	 * Set this to be the number of times to configure a foreign object's foreign object. If you query for A and it has
	 * an foreign field B which has an foreign field C ..., then a lot of configuration information is being stored. If
	 * each of these fields is auto-refreshed, then querying for A could get expensive. Setting this value to 1 will
	 * mean that when you query for A, B will be auto-refreshed, but C will just have its id field set. This also works
	 * if A has an auto-refresh field B which has an auto-refresh field A.
	 * 
	 * <p>
	 * <b>NOTE:</b> Increasing this value will result in more database transactions whenever you query for A, so use
	 * carefully.
	 * </p>
	 */
	int maxForeignAutoRefreshLevel() default DEFAULT_MAX_FOREIGN_AUTO_REFRESH_LEVEL;

	/**
	 * Allows you to set a custom persister class to handle this field. This class must have a getSingleton() static
	 * method defined which will return the singleton persister.
	 * 
	 * @see DataPersister
	 */
	Class<? extends DataPersister> persisterClass() default VoidType.class;

	/**
	 * If this is set to true then inserting an object with the ID field already set will not override it with a
	 * generated-id. This is useful when you have a table where items sometimes have IDs and sometimes need them
	 * generated. This only works if the database supports this behavior and if {@link #generatedId()} is also true for
	 * the field.
	 */
	boolean allowGeneratedIdInsert() default false;

	/**
	 * Specify the SQL necessary to create this field in the database. This can be used if you need to tune the schema
	 * to enable some per-database feature or to override the default SQL generated.
	 */
	String columnDefinition() default DEFAULT_STRING;
}
