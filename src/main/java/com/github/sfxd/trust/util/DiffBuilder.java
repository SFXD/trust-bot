// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiffBuilder {
    private final List<Diff<?>> diffs = new ArrayList<>();

    public <T> DiffBuilder append(String field, T left, T right) {
        if (!Objects.equals(left, right)) {
            this.diffs.add(new Diff<>(field, left, right));
        }

        return this;
    }

    public List<Diff<?>> build() {
        return Collections.unmodifiableList(this.diffs);
    }
}
