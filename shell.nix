{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
  buildInputs = [
    openal
    alsa-oss
    openjdk
  ];

  shellHook = ''
    echo "You are now in the development environment for APSGame"
  '';
}
# let dependencies = rec {
#
# };
# in with dependencies;
# stdenv.mkDerivation rec {
#   version = "v0.6.2a";
#   pname = "shattered-pixel-dungeon-gdx";
#   name = "${pname}-${version}";
#
#   desktopItem = makeDesktopItem {
#     name = "ShatteredPD";
#     exec = "ShatteredPD";
#     icon = "ShatteredPD";
#     comment = "Traditional roguelike game with pixel-art graphics and simple interface";
#     desktopName = "ShatteredPD";
#     genericName = "ShatteredPD";
#     categories = "Game;";
#   };
#
#   src = fetchurl {
#     url = "https://github.com/00-Evan/${pname}/releases/download/${version}/ShatteredPD.Desktop.${version}.jar";
#     sha256 = "05kx95z1nd6zg49f3naxvghah9vzs2y89q4zwvnam7mg0mk336ls";
#   };
#
#   phases = "installPhase";
#
#   installPhase = ''
#     set -x
#     mkdir -pv $out/bin $out/opt/${pname}
#     cp -v $src $out/opt/${pname}/ShatteredPD.Desktop.${version}.jar
#     cat > $out/bin/ShatteredPD << EOF
#     #!${stdenv.shell}
#     export LD_LIBRARY_PATH=\$LD_LIBRARY_PATH:${stdenv.lib.makeLibraryPath [ openal ]}
#     ${"${alsaOss}/bin/aoss"} \
#       ${jre}/bin/java -jar $out/opt/${pname}/ShatteredPD.Desktop.${version}.jar
#     EOF
#     chmod +x $out/bin/ShatteredPD
#
#     mkdir -p $out/share/applications
#     ln -s ${desktopItem}/share/applications/* $out/share/applications/
#
#     ${openjdk}/bin/jar xf $out/opt/${pname}/ShatteredPD.Desktop.${version}.jar ic_launcher_32.png
#     install -D ic_launcher_32.png $out/share/icons/hicolor/32x32/apps/ShatteredPD.png
#   '';
#
#   meta = with stdenv.lib; {
#     description = "Traditional roguelike game with pixel-art graphics and simple interface";
#     homepage = https://github.com/00-Evan/shattered-pixel-dungeon-gdx;
#     license = licenses.gpl3;
#     maintainers = with maintainers; [ ];
#   };
# }
