import React from 'react';
import { requireNativeComponent, ViewProps } from 'react-native';

interface GDTSplashProps extends ViewProps {
  posId: string;
  fetchDelay?: number;
  onFailToReceived: (error: Error) => void;
  onNextAction: () => void;
}

const GDTSplashViewManager = requireNativeComponent<GDTSplashProps>(
  'GDTSplashViewManager'
);

const GDTSplash: React.FC<GDTSplashProps> = ({
  posId,
  onFailToReceived,
  ...otherProps
}) => {
  const _onFailToReceived = (event: any) => {
    onFailToReceived && onFailToReceived(new Error(event.nativeEvent.error));
  };

  React.useEffect(() => {
    if (!posId) {
      throw new Error("BannerAd: 'posId' expected a valid string pos ID.");
    }
  }, [posId]);

  // StatusBar.setHidden(true);
  return (
    <GDTSplashViewManager
      posId={posId}
      {...otherProps}
      onFailToReceived={_onFailToReceived}
    />
  );
};

export default GDTSplash;
