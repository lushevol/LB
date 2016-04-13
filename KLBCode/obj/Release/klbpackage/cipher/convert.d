obj/Release/klbpackage/cipher/convert.o obj/Release/klbpackage/cipher/convert.d: src/cipher/convert.cpp src/cipher/share/include.h \
 src/cipher/cipher.h
	rm -f obj/Release/klbpackage/cipher/convert.d
	make -f make.do obj/Release/klbpackage/cipher/convert.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbpackage/cipher/convert.o src/cipher/convert.cpp
%.h: fail
	
.PHONY:fail
fail:
	
