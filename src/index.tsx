import { NativeModules } from 'react-native';

type GdtAdType = {
  init(appId: string): Promise<boolean>;
  showUnifiedInterstitialAD(posId: string, popPu: boolean): Promise<boolean>;
};

const { GdtAd } = NativeModules;

export default GdtAd as GdtAdType;

export { default as GDTUnifiedBanner } from './GDTUnifiedBanner';
export { default as GDTSplash } from './GDTSplash';
