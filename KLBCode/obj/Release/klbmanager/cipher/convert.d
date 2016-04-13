obj/Release/klbmanager/cipher/convert.o obj/Release/klbmanager/cipher/convert.d: src/cipher/convert.cpp src/cipher/share/include.h \
 src/cipher/cipher.h
	rm -f obj/Release/klbmanager/cipher/convert.d
	make -f make.do obj/Release/klbmanager/cipher/convert.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbmanager/cipher/convert.o src/cipher/convert.cpp
%.h: fail
	
.PHONY:fail
fail:
	
