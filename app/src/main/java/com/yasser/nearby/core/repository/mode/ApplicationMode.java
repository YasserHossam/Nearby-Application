package com.yasser.nearby.core.repository.mode;

import androidx.annotation.IntDef;

@IntDef({ApplicationMode.SINGLE, ApplicationMode.REALTIME})
public @interface ApplicationMode {
    int SINGLE = 1;
    int REALTIME = 2;
}
