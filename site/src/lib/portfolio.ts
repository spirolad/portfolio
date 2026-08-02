export type PortfolioProfile = {
  name: string;
  email: string;
  currentPosition: string;
  photo?: string | null;
};

export type Education = {
  id?: number;
  institution: string;
  degree: string;
  startDate: string;
  endDate?: string | null;
};

export type Experience = {
  id?: number;
  company: string;
  position: string;
  mission?: string[] | null;
  startDate: string;
  endDate?: string | null;
};

export type Project = {
  id?: number;
  name: string;
  summary?: string | null;
  description?: string | null;
  link?: string | null;
  screenshots?: string[] | null;
  technologies?: string[] | null;
};

export type Category = {
  id?: number;
  name: string;
};

export type Skill = {
  id?: number;
  name: string;
  category: Category;
};

export type Portfolio = {
  generalInfo: PortfolioProfile;
  educations: Education[];
  experiences: Experience[];
  projects: Project[];
  skills: Skill[];
};

const DEFAULT_BASE_URL = 'http://localhost:8080';

export function getApiBaseUrl(): string {
  return (import.meta.env.PUBLIC_PORTFOLIO_API_URL ?? import.meta.env.PORTFOLIO_API_URL ?? DEFAULT_BASE_URL).replace(/\/$/, '');
}

export function toImageSrc(value?: string | null): string | null {
  if (!value) return null;
  const clean = value.includes(',') ? value.split(',')[1] ?? '' : value;
  if (!clean) return null;
  const mime = clean.startsWith('/9j/') ? 'image/jpeg' : clean.startsWith('iVBORw0KG') ? 'image/png' : 'image/png';
  return `data:${mime};base64,${clean}`;
}

export function formatDate(value?: string | null): string {
  if (!value) return 'Présent';
  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) return value;
  return new Intl.DateTimeFormat('fr-FR', { month: 'short', year: 'numeric' }).format(parsed);
}

export function formatPeriod(startDate: string, endDate?: string | null): string {
  return `${formatDate(startDate)} — ${formatDate(endDate ?? null)}`;
}

export function getPortfolioSubtitle(portfolio: Portfolio): string {
  const projects = portfolio.projects.length;
  const skills = portfolio.skills.length;
  const experiences = portfolio.experiences.length;
  return `${experiences} expérience${experiences > 1 ? 's' : ''} · ${projects} projet${projects > 1 ? 's' : ''} · ${skills} compétence${skills > 1 ? 's' : ''}`;
}

