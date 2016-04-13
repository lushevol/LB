obj/Debug/klbroute/route/libstatic.o obj/Debug/klbroute/route/libstatic.d: src/route/libstatic.cpp src/route/share/include.h \
 src/route/share/debug.h src/route/libstatic.h
	rm -f obj/Debug/klbroute/route/libstatic.d
	make -f make.do obj/Debug/klbroute/route/libstatic.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__DEBUG__ -c -o obj/Debug/klbroute/route/libstatic.o src/route/libstatic.cpp
%.h: fail
	
.PHONY:fail
fail:
	
