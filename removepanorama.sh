#!/bin/bash
set -e
for i in assets/resource_packs/vanilla/textures/ui/panorama_*.png
do
	cp onepixel.png $i
done
