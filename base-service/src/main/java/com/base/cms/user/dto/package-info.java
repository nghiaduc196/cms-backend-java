/**
 * DTO (Data Transfer Object) package – tổ chức theo domain và vai trò.
 * <ul>
 *   <li>{@link com.base.cms.user.dto.common} – DTO dùng chung (base response với audit fields)</li>
 *   <li>{@link com.base.cms.user.dto.user} – DTO cho User (request/response)</li>
 *   <li>{@link com.base.cms.user.dto.dict} – DTO cho SysDict (request/response/query)</li>
 *   <li>{@link com.base.cms.user.dto.dictitem} – DTO cho SysDictItem (request/response)</li>
 * </ul>
 * Quy ước: Request = input (create/update), Response = output (kế thừa BaseAuditResponseDto khi có audit),
 * Query = filter/search (phân trang, tìm kiếm).
 */
package com.base.cms.user.dto;
