package org.ecom.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents page information of response.
 */

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PageInfo {

   private int pageSize;

   private int pageNumber;

   private int totalPages;

   private long totalElements;
}
