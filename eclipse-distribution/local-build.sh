mvn clean package -Pe427 -Psnapshot -Dsigning.skip=true -Dhttpclient.retry-max=20 -Dmaven.test.skip=true -Declipse.p2.mirrors=false -Dtycho.localArtifacts=ignore -Dorg.eclipse.equinox.p2.transport.ecf.retry=5 -Dskip.osx.signing=true -Dskip.win.signing=true -Dskip.osx.notarizing=true

