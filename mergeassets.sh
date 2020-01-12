#!/bin/bash
cp assets017/fonts/minecraft.ttf assets/fonts/
#rm -r assets/sounds
#rm -r assets/resource_packs/cartoon assets/resource_packs/city assets/resource_packs/education assets/resource_packs/fantasy assets/resource_packs/natural assets/resource_packs/plastic
rm -r assets/structures assets/resource_packs/education assets/resource_packs/edu_loading_screens assets/resource_packs/vanilla_vr assets/resource_packs/vanilla/sounds
cp /home/zhuowei/Documents/repos/mercator/portingarea/slick/items/* assets/resource_packs/vanilla/textures/items/
cp /home/zhuowei/Documents/repos/mercator/portingarea/slick/blocks/* assets/resource_packs/vanilla/textures/blocks/
#todo: make everything rideable by default
