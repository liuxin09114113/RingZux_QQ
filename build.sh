#!/usr/bin/bash

path=$(pwd)

echo $path
if [[ ! -e $path/bin ]];then
    mkdir $path/bin
fi

if [[ ! -e $path/build ]];then
    mkdir $path/build
fi


if [[ -e $path/bin/out.jar ]];then
    rm $path/bin/out.jar
fi

if [[ -e $path/build/source.txt ]];then
    rm -r $path/build/*
fi




filelist(){
for file in `ls $1|grep -v ".bak"`
  do
    if [ -d $1"/"$file ]
    then
      filelist $1"/"$file
    else
      local file_path=$1"/"$file 
      if echo $file_path|grep "MANIFEST.MF">/dev/null;then
      c=c
      else
          echo $file_path >> $path/build/source.txt
      fi
    fi
  done
}

filelist $path/src

libs=""

for java_lib in $(ls $path/libs);do
if [[ libs != "" ]];then
libs=$libs:$path/libs/$java_lib
else
libs=$path/libs/$java_lib
fi
done



javac -encoding utf-8 -Xlint:unchecked -d $path/build -classpath $libs @$path/build/source.txt

unzip $path/libs/lanterna.jar -d $path/build >/dev/null 2>&1
cd $path/build
jar cmf  $path/src/MANIFEST.MF  $path/bin/out.jar ./*


