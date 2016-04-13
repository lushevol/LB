obj/Debug/klbpackage/cipher/key.o obj/Debug/klbpackage/cipher/key.d: src/cipher/key.cpp src/cipher/share/include.h \
 src/cipher/share/debug.h src/cipher/cipher.h
	rm -f obj/Debug/klbpackage/cipher/key.d
	make -f make.do obj/Debug/klbpackage/cipher/key.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__DEBUG__ -c -o obj/Debug/klbpackage/cipher/key.o src/cipher/key.cpp
%.h: fail
	
.PHONY:fail
fail:
	
