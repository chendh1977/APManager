package com.android.networkmanager.Item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class ClientItem {

    public String mMacAddress;
    public String mApInstanceIdentifier;

    public ClientItem(@NonNull String macAddress, @NonNull String apInstanceIdentifier) {
        this.mMacAddress = macAddress;
        this.mApInstanceIdentifier = apInstanceIdentifier;
    }

    @Override
    public boolean equals(@Nullable Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        if (!(otherObj instanceof ClientItem)) {
            return false;
        }
        ClientItem other = (ClientItem) otherObj;
        return Objects.equals(mMacAddress, other.mMacAddress)
                && Objects.equals(mApInstanceIdentifier, other.mApInstanceIdentifier);
    }
}
