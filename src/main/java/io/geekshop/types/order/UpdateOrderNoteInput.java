/*
 * Copyright (c) 2020 GeekShop.
 * All rights reserved.
 */

package io.geekshop.types.order;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateOrderNoteInput {
    private Long noteId;
    private String note;
    private Boolean privateOnly;
}
