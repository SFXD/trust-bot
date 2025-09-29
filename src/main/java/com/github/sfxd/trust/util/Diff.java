// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.util;

public record Diff<T>(String field, T left, T right) {
}
