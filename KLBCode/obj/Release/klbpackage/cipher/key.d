obj/Release/klbpackage/cipher/key.o obj/Release/klbpackage/cipher/key.d: src/cipher/key.cpp src/cipher/share/include.h src/cipher/cipher.h
	rm -f obj/Release/klbpackage/cipher/key.d
	make -f make.do obj/Release/klbpackage/cipher/key.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbpackage/cipher/key.o src/cipher/key.cpp
%.h: fail
	
.PHONY:fail
fail:
	
