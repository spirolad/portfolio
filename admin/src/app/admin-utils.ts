export function linesFromText(value: string | null | undefined): string[] {
  return (value ?? '')
    .split(/\r?\n/)
    .map((item) => item.trim())
    .filter((item) => item.length > 0);
}

export function textFromLines(values: Array<string | null | undefined> | null | undefined): string {
  return (values ?? [])
    .filter((value): value is string => typeof value === 'string' && value.length > 0)
    .join('\n');
}

export function fileToBase64(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => {
      const result = String(reader.result ?? '');
      const base64Value = result.includes(',') ? result.split(',')[1] ?? '' : result;
      resolve(base64Value);
    };
    reader.onerror = () => reject(reader.error ?? new Error('Impossible de lire le fichier'));
    reader.readAsDataURL(file);
  });
}

export function mimeTypeFromBase64(value: string | null | undefined): string {
  const base = (value ?? '').includes(',') ? (value ?? '').split(',')[1] ?? '' : (value ?? '');
  if (base.startsWith('/9j/')) return 'image/jpeg';
  if (base.startsWith('iVBORw0KG')) return 'image/png';
  if (base.startsWith('R0lGOD')) return 'image/gif';
  // fallback to png
  return 'image/png';
}

export function errorMessage(error: unknown): string {
  if (typeof error === 'string') {
    return error;
  }

  if (error && typeof error === 'object') {
    const maybeMessage = (error as { message?: unknown }).message;
    if (typeof maybeMessage === 'string' && maybeMessage.trim().length > 0) {
      return maybeMessage;
    }
  }

  return 'Erreur lors de la communication avec l API';
}
