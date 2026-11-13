import { Directory, Filesystem } from '@capacitor/filesystem';

export const saveAvatar = async (id: string, dataUrl: string): Promise<string> => {
  try {
    const base64 = dataUrl.split(',')[1];

    if (!base64) {
      throw new Error('Invalid image data');
    }

    const result = await Filesystem.writeFile({
      path: `avatars/${id}.jpg`,
      data: base64,
      directory: Directory.Data,
      recursive: true
    });

    console.log('Filesystem write result:', result);
    return result.uri;
  } catch (error) {
    console.error('Failed to write avatar:', error);
    throw error;
  }
};

export const loadAvatar = async (id: string): Promise<string | undefined> => {
  try {
    const result = await Filesystem.readFile({
      path: `avatars/${id}.jpg`,
      directory: Directory.Data
    });

    return `data:image/jpeg;base64,${result.data}`;
  } catch {
    return undefined;
  }
};

export const renameAvatar = async (oldId: string, newId: string) => {
  try {
    await Filesystem.rename({
      from: `avatars/${oldId}.jpg`,
      to: `avatars/${newId}.jpg`,
      directory: Directory.Data
    });
  } catch (error) {
    console.error('Could not move avatar:', error);
  }
};