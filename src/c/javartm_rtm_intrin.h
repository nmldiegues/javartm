/*
 * javartm: a Java library for Restricted Transactional Memory
 * Copyright (C) 2013 Ivo Anjo <ivo.anjo@ist.utl.pt>
 * Copyright (C) 2013 João Fernandes <mail@joaofernandes.eu>
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

// Alternative implementation of RTM intrinsics for GCC < 4.8 and other compilers
// Based on work by João Fernandes <mail@joaofernandes.eu>, thanks a lot!

#ifndef __RTM__
#define __RTM__

#include <stdint.h>

#define GCC_VERSION (__GNUC__ * 10000 + __GNUC_MINOR__ * 100 + __GNUC_PATCHLEVEL__)
#if GCC_VERSION < 40500
	#warning GCC version >= 4.5 is needed for asm goto support, build will probably fail, see gcc.gnu.org/onlinedocs/gcc/Extended-Asm.html .
#endif

#define __XBEGIN(label) \
	asm volatile goto(".byte 0xc7,0xf8 ; .long %l0-1f\n1:" ::: "eax","memory" : label)
#define __XFAIL_STATUS(label, status) \
	label: asm volatile("" : "=a" (status) :: "memory")
#define _xbegin() ({ \
	__label__ failure; \
	uint32_t status = 0xFFFFFFFF; \
	__XBEGIN(failure); \
	__XFAIL_STATUS(failure, status); \
	status; })

#define _xtest() ({ \
	char o = 0 ; \
	__asm__ __volatile__ (".byte 0x0f,0x01,0xd6 ; setnz %0" : "+r" (o)::"memory"); \
	o; })

#define _xend() \
	asm volatile(".byte 0x0f,0x01,0xd5" ::: "memory")

#define _xabort(status) \
	asm volatile(".byte 0xc6,0xf8,%P0" :: "i" (status))

#define bit_RTM (1 << 11)

#define _XBEGIN_STARTED  (-1)

#define _XABORT_EXPLICIT (1 << 0)
#define _XABORT_RETRY    (1 << 1)
#define _XABORT_CONFLICT (1 << 2)
#define _XABORT_CAPACITY (1 << 3)
#define _XABORT_DEBUG    (1 << 4)
#define _XABORT_NESTED   (1 << 5)

#endif // __RTM__
