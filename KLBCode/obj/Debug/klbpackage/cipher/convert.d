obj/Debug/klbpackage/cipher/convert.o obj/Debug/klbpackage/cipher/convert.d: src/cipher/convert.cpp src/cipher/share/include.h \
 src/cipher/share/debug.h src/cipher/cipher.h
	rm -f obj/Debug/klbpackage/cipher/convert.d
	make -f make.do obj/Debug/klbpackage/cipher/convert.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__DEBUG__ -c -o obj/Debug/klbpackage/cipher/convert.o src/cipher/convert.cpp
%.h: fail
	
.PHONY:fail
fail:
	
