/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import GdtAd, { GDTUnifiedBanner, GDTSplash } from 'react-native-gdt-adnet';

export default function App() {
  const [init, setInit] = React.useState(false);

  React.useEffect(() => {
    (async () => {
      const res = await GdtAd.init('1101152570');
      // GdtAd.showUnifiedInterstitialAD('3040652898151811', true);
      console.log(res);
      setInit(true);
    })();
  }, []);

  return (
    <View style={styles.container}>
      {/* {init && <GDTUnifiedBanner posId="4080052898050840" />} */}
      {init && (
        <GDTSplash
          style={{ flex: 1 }}
          onFailToReceived={(error) => {
            console.log(error);
          }}
          onNextAction={() => {
            //下一步路由动作
            console.log('onNextAction');
          }}
          posId="8863364436303842593"
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
