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

// Dummy versions of javartm native methods that are used for machines without rtm support
// Doing this avoids having extra tests in the normal lib, which are bad for methods that are
// supposed to run with active transactions

// Needed for JNI
#include <jni.h>
#include "javartm_Transaction.h"

// Other stuff
#include <stdio.h>

void throwException(JNIEnv *env) {
	jclass excClass = (*env)->FindClass(env, "java/lang/RuntimeException");
	if (!excClass) return;
	(*env)->ThrowNew(env, excClass, "No hardware RTM support is available on this machine");
}

JNIEXPORT jboolean JNICALL Java_javartm_Transaction_inTransaction(JNIEnv *env, jclass cls) {
	throwException(env);
	return 0;
}

JNIEXPORT jint JNICALL Java_javartm_Transaction_begin(JNIEnv *env, jclass cls) {
	throwException(env);
	return 0;
}

JNIEXPORT void JNICALL Java_javartm_Transaction_commit(JNIEnv *env, jclass cls) {
	throwException(env);
}

JNIEXPORT void JNICALL Java_javartm_Transaction_abort__(JNIEnv *env, jclass cls) {
	throwException(env);
}

JNIEXPORT void JNICALL Java_javartm_Transaction_abort__J(JNIEnv *env, jclass cls, jlong reason) {
	throwException(env);
}
