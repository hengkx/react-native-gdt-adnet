import React from 'react';
import {
  requireNativeComponent,
  ViewProps,
  useWindowDimensions,
} from 'react-native';

interface GDTUnifiedBannerProps extends ViewProps {
  posId: string;
}
const GDTUnifiedBannerView = requireNativeComponent<GDTUnifiedBannerProps>(
  'GDTUnifiedBannerViewManager'
);

const GDTUnifiedBanner: React.FC<GDTUnifiedBannerProps> = ({ posId }) => {
  const { width } = useWindowDimensions();
  React.useEffect(() => {
    if (!posId) {
      throw new Error("BannerAd: 'posId' expected a valid string pos ID.");
    }
  }, [posId]);

  return (
    <GDTUnifiedBannerView
      posId={posId}
      style={{
        width,
        height: Math.round(width / 6.4),
      }}
    />
  );
};

export default GDTUnifiedBanner;
