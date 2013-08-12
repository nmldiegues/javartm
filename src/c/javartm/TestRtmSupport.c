/*
 * javartm: a Java library for Restricted Transactional Memory
 * Copyright (C) 2013 Ivo Anjo <ivo.anjo@ist.utl.pt>
 *
 * This file is part of javartm.
 *
 * javartm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * javartm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with javartm.  If not, see <http://www.gnu.org/licenses/>.
 */

// Small test library, allowing rtm support to be tested without loading the rest of the rtm code

// Used to test cpuid support
#include <x86intrin.h>
#include <cpuid.h>

// Needed for JNI
#include <jni.h>
#include "javartm_TestRtmSupport.h"

#define bit_RTM (1 << 11)

JNIEXPORT jboolean JNICALL Java_javartm_TestRtmSupport_rtmAvailable(JNIEnv *env, jclass cls) {
	unsigned int eax, ebx, ecx, edx;
	if (__get_cpuid_max(0, NULL) >= 7) {
		__cpuid_count(7, 0, eax, ebx, ecx, edx);
		if (ebx & bit_RTM) return 1;
	}
	return 0;
}
