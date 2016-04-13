obj/Debug/klbpackage/package/main.o obj/Debug/klbpackage/package/main.d: src/package/main.cpp src/package/share/include.h \
 src/package/share/debug.h src/package/share/utility.h \
 src/package/share/include.h src/package/cipher/cipher.h \
 src/package/licence/licence.h src/package/licence/share/include.h
	rm -f obj/Debug/klbpackage/package/main.d
	make -f make.do obj/Debug/klbpackage/package/main.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__GENERATOR__ -D__DEBUG__ -c -o obj/Debug/klbpackage/package/main.o src/package/main.cpp
%.h: fail
	
.PHONY:fail
fail:
	
