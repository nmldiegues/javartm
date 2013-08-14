# javartm library README

The javartm library allows Java/JVM users to use the "Restricted Transactional Memory" support available on some Intel CPUs. See [here][inteltsx] for more details.

In practice, this allows code running on the JVM to enjoy the benefits of Hardware Transactional Memory.

javartm is distributed under the terms of the GNU LGPLv3; see the COPYING and COPYING.LESSER files for more details.

## Hardware Requirements

As of this writing only CPUs based on the Intel Haswell architecture include the necessary hardware for RTM. See [here][inteltsx] for Intel's announcement, and [here][tsxspecs] for the specifications.

Testing on other CPUs can be done using the [Intel Software Development Emulator][intelsde].

## Building

Building is done with ant, and requires both the [ant-cpptasks][ant-cpptasks] and the [ant-contrib][ant-contrib] libraries.

Note that both the `ant-contrib-cpptasks` and the `ant-contrib` packages in Ubuntu are not installed to the right places, and ant will not be able to use them automatically. (To fix it, run `sudo ln -s /usr/share/java/ant-contrib-cpptasks.jar /usr/share/ant/lib/` && `sudo ln -s /usr/share/java/ant-contrib.jar /usr/share/ant/lib/`)

Gcc's native suport for RTM (for versions >= 4.8) is used if available; otherwise javartm includes a fallback alternative.

I plan on providing binary packages of javartm soon. If they aren't up yet, feel free to mail me to ask for them.

## Testing with Intel SDE

The script `scripts/runpin.sh` can be used to run applications with javartm using the Intel SDE, which is supported on all x86-64 CPUs.

## Who am I

My name is [Ivo Anjo][insthome] and I am a PhD student working on Software Transactional Memory and Speculative Parallelization at the [Software Engineering Group][eswweb] (ESW) at INESC-ID Lisboa.

Feel free to contact me about javartm at <ivo.anjo@ist.utl.pt>.

Some other software I am involved with and that might interest you is the [Java Versioned Software Transactional Memory (JVSTM)][jvstm], a Java library implementing TM and the [Fénix Framework][fenixf], a framework for building web-applications combining transactional memory and persistence.

Thanks for reading this far! :)

[inteltsx]: http://software.intel.com/en-us/blogs/2012/02/07/transactional-synchronization-in-haswell
[intelsde]: http://software.intel.com/en-us/articles/intel-software-development-emulator
[tsxspecs]: http://software.intel.com/sites/default/files/m/9/2/3/41604 "Intel Architecture Instruction Set Extensions Programming Reference"
[ant-cpptasks]: http://ant-contrib.sourceforge.net/cpptasks/index.html
[ant-contrib]: http://ant-contrib.sourceforge.net/
[insthome]: https://fenix.ist.utl.pt/homepage/ist155460
[eswweb]: http://www.esw.inesc-id.pt/
[jvstm]: http://esw.inesc-id.pt/git/jvstm.git/
[fenixf]: https://fenix-ashes.ist.utl.pt/trac/fenix-framework
