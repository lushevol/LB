obj/Debug/klbmanager/share/debug.o obj/Debug/klbmanager/share/debug.d: src/share/debug.cpp src/share/include.h src/share/debug.h
	rm -f obj/Debug/klbmanager/share/debug.d
	make -f make.do obj/Debug/klbmanager/share/debug.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__SERVER__ -D__DEBUG__ -c -o obj/Debug/klbmanager/share/debug.o src/share/debug.cpp
%.h: fail
	
.PHONY:fail
fail:
	
