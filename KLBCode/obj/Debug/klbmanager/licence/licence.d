obj/Debug/klbmanager/licence/licence.o obj/Debug/klbmanager/licence/licence.d: src/licence/licence.cpp src/licence/cipher/cipher.h \
 src/licence/licence.h src/licence/share/include.h \
 src/licence/share/debug.h
	rm -f obj/Debug/klbmanager/licence/licence.d
	make -f make.do obj/Debug/klbmanager/licence/licence.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/licence/licence.o src/licence/licence.cpp
%.h: fail
	
.PHONY:fail
fail:
	
