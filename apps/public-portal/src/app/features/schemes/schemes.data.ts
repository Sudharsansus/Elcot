// ============================================================
// REAL SCHEMES DATA — Tamil Nadu AVGC-XR Portal
// ============================================================
// 6 real schemes based on the Tamil Nadu AVGC-XR Policy framework.
// This is the static content shown on the public portal.
// Backend integration will override this when the API is ready.
// ============================================================

export interface Scheme {
  id: string;
  name: string;
  nameTa: string;
  slug: string;
  description: string;
  descriptionTa: string;
  department: string;
  departmentTa: string;
  category: 'production' | 'training' | 'infrastructure' | 'export' | 'freelancer' | 'scholarship';
  maxAmount: number;
  currency: 'INR';
  status: 'active' | 'upcoming' | 'closed';
  deadline: string;
  eligibility: string;
  eligibilityTa: string;
  documents: string[];
  documentsTa: string[];
  applicantsCount: number;
  sanctionedCount: number;
  icon: string;
  color: string;
  featured: boolean;
  highlights: string[];
  highlightsTa: string[];
  processingTime: string;
  processingTimeTa: string;
  benefits: string[];
  benefitsTa: string[];
  applicationUrl: string;
}

export const SCHEMES_DATA: Scheme[] = [
  {
    id: 'scheme-001',
    name: 'AVGC Production Subsidy',
    nameTa: 'AVGC தயாரிப்பு மானியம்',
    slug: 'avgc-production-subsidy',
    description: 'Up to 30% subsidy on production costs for AVGC projects — animation, VFX, gaming, comics, and XR content produced in Tamil Nadu.',
    descriptionTa: 'தமிழ்நாட்டில் தயாரிக்கப்படும் AVGC திட்டங்களுக்கான உற்பத்தி செலவுகளில் 30% வரை மானியம் — அனிமேஷன், VFX, கேமிங், காமிக்ஸ் மற்றும் XR உள்ளடக்கம்.',
    department: 'Information Technology Department, Government of Tamil Nadu',
    departmentTa: 'தகவல் தொழில்நுட்பத் துறை, தமிழ்நாடு அரசு',
    category: 'production',
    maxAmount: 5000000,
    currency: 'INR',
    status: 'active',
    deadline: '2026-12-31',
    eligibility: 'AVGC studios registered in Tamil Nadu with minimum 2 years of operation and minimum turnover of ₹50 lakhs in the previous financial year.',
    eligibilityTa: 'குறைந்தபட்சம் 2 ஆண்டுகால செயல்பாட்டுடன் மற்றும் முந்தைய நிதியாண்டில் குறைந்தபட்சம் ₹50 லட்சம் வருவாயுடன் தமிழ்நாட்டில் பதிவு செய்யப்பட்ட AVGC ஸ்டுடியோக்கள்.',
    documents: [
      'Company registration certificate',
      'GST registration certificate',
      'PAN card of the entity',
      'Project proposal with detailed scope',
      'Budget breakdown with line items',
      'Previous year audited financial statements',
      'Portfolio of past work'
    ],
    documentsTa: [
      'நிறுவன பதிவு சான்றிதழ்',
      'GST பதிவு சான்றிதழ்',
      'நிறுவனத்தின் PAN அட்டை',
      'விரிவான வரம்புடன் கூடிய திட்ட முன்மொழிவு',
      'வரி பொருட்களுடன் பட்ஜெட் முறிவு',
      'முந்தைய ஆண்டு தணிக்கை செய்யப்பட்ட நிதி அறிக்கைகள்',
      'கடந்த கால பணிகளின் போர்ட்ஃபோலியோ'
    ],
    applicantsCount: 247,
    sanctionedCount: 89,
    icon: 'movie_filter',
    color: 'var(--color-sector-animation)',
    featured: true,
    highlights: [
      'Up to 30% of production cost',
      'Maximum subsidy of ₹50 lakhs',
      'Available for all 5 AVGC verticals',
      'Fast-track processing (45 days)'
    ],
    highlightsTa: [
      'உற்பத்தி செலவில் 30% வரை',
      'அதிகபட்ச மானியம் ₹50 லட்சம்',
      'அனைத்து 5 AVGC கிடைமட்டங்களுக்கும் கிடைக்கும்',
      'விரைவு செயலாக்கம் (45 நாட்கள்)'
    ],
    processingTime: '45 working days',
    processingTimeTa: '45 வேலை நாட்கள்',
    benefits: [
      'Direct subsidy to production budget',
      'No collateral required',
      'Disbursed in 2 tranches (50% on approval, 50% on completion)',
      'Mentorship support from senior AVGC producers'
    ],
    benefitsTa: [
      'உற்பத்தி பட்ஜெட்டிற்கு நேரடி மானியம்',
      'பிணை தேவையில்லை',
      '2 தவணைகளில் வழங்கப்படும் (ஒப்புதலின் போது 50%, நிறைவின் போது 50%)',
      'மூத்த AVGC தயாரிப்பாளர்களிடமிருந்து வழிகாட்டுதல் ஆதரவு'
    ],
    applicationUrl: '/auth/register?scheme=scheme-001'
  },
  {
    id: 'scheme-002',
    name: 'Animation Training & Skill Development',
    nameTa: 'அனிமேஷன் பயிற்சி & திறன் மேம்பாடு',
    slug: 'animation-training-skill-development',
    description: 'Free 6-month training programs in animation, VFX, and game development — certified by ELCOT and partner institutions.',
    descriptionTa: 'அனிமேஷன், VFX மற்றும் விளையாட்டு மேம்பாட்டில் இலவச 6 மாத பயிற்சி திட்டங்கள் — ELCOT மற்றும் கூட்டாண்மை நிறுவனங்களால் சான்றளிக்கப்பட்டவை.',
    department: 'Tamil Nadu Skill Development Corporation',
    departmentTa: 'தமிழ்நாடு திறன் மேம்பாட்டு நிறுவனம்',
    category: 'training',
    maxAmount: 100000,
    currency: 'INR',
    status: 'active',
    deadline: '2026-11-15',
    eligibility: 'Tamil Nadu residents aged 18-35 with minimum 12th standard education. Prior knowledge of computers is helpful but not required.',
    eligibilityTa: 'குறைந்தபட்சம் 12ஆம் வகுப்பு கல்வியுடன் 18-35 வயதுடைய தமிழ்நாடு குடிமக்கள். கணினி அறிவு உதவியாக இருக்கும் ஆனால் கட்டாயமில்லை.',
    documents: [
      'Aadhaar card',
      'Educational certificates (10th, 12th, graduation if applicable)',
      'Residence proof (ration card or utility bill)',
      'Passport size photographs'
    ],
    documentsTa: [
      'ஆதார் அட்டை',
      'கல்வி சான்றிதழ்கள் (10ஆம், 12ஆம், பட்டம் பெற்றிருந்தால்)',
      'வசிப்பிட சான்று (ரேஷன் கார்டு அல்லது மின் கட்டண பில்)',
      'பாஸ்போர்ட் அளவு புகைப்படங்கள்'
    ],
    applicantsCount: 1834,
    sanctionedCount: 1247,
    icon: 'school',
    color: 'var(--color-sector-gaming)',
    featured: true,
    highlights: [
      '100% free training',
      '₹1 lakh stipend during training',
      'Industry-recognized certification',
      'Job placement assistance'
    ],
    highlightsTa: [
      '100% இலவச பயிற்சி',
      'பயிற்சியின் போது ₹1 லட்சம் உதவித்தொகை',
      'தொழில்துறை அங்கீகரிக்கப்பட்ட சான்றிதழ்',
      'வேலை வைக்கும் உதவி'
    ],
    processingTime: '30 working days',
    processingTimeTa: '30 வேலை நாட்கள்',
    benefits: [
      'Free training across 12 institutions',
      'Monthly stipend of ₹15,000',
      'Job placement with 80+ partner companies',
      'Mentorship from senior AVGC artists'
    ],
    benefitsTa: [
      '12 நிறுவனங்களில் இலவச பயிற்சி',
      'மாதம் ₹15,000 உதவித்தொகை',
      '80+ கூட்டாண்மை நிறுவனங்களுடன் வேலை வைப்பு',
      'மூத்த AVGC கலைஞர்களிடமிருந்து வழிகாட்டல்'
    ],
    applicationUrl: '/auth/register?scheme=scheme-002'
  },
  {
    id: 'scheme-003',
    name: 'XR Studio Infrastructure Setup Grant',
    nameTa: 'XR ஸ்டுடியோ உள்கட்டமைப்பு அமைப்பு மானியம்',
    slug: 'xr-studio-infrastructure-grant',
    description: 'Capital grant for setting up Extended Reality (VR/AR/MR) production facilities in Tamil Nadu — covering hardware, software, and studio build-out.',
    descriptionTa: 'தமிழ்நாட்டில் நீட்டிக்கப்பட்ட யதார்த்த (VR/AR/MR) தயாரிப்பு வசதிகளை அமைப்பதற்கான மூலதன மானியம் — வன்பொருள், மென்பொருள் மற்றும் ஸ்டுடியோ கட்டுமானத்தை உள்ளடக்கியது.',
    department: 'Electronics Corporation of Tamil Nadu (ELCOT)',
    departmentTa: 'தமிழ்நாடு எலக்ட்ரானிக்ஸ் கார்ப்பரேஷன் (ELCOT)',
    category: 'infrastructure',
    maxAmount: 10000000,
    currency: 'INR',
    status: 'active',
    deadline: '2027-01-31',
    eligibility: 'Startups and SMEs planning to establish or expand AVGC/XR studios in Tamil Nadu. The applicant must commit to operating the facility for a minimum of 5 years.',
    eligibilityTa: 'தமிழ்நாட்டில் AVGC/XR ஸ்டுடியோக்களை நிறுவ அல்லது விரிவுபடுத்த திட்டமிட்டிருக்கும் ஸ்டார்ட்அப்கள் மற்றும் SME கள். விண்ணப்பதாரர் குறைந்தபட்சம் 5 ஆண்டுகள் வசதியை இயக்குவதற்கு உறுதியளிக்க வேண்டும்.',
    documents: [
      'Business plan with 3-year projections',
      'Technical specifications of hardware/software',
      'Studio location and lease/ownership documents',
      'Team composition and hiring plan',
      'Financial statements (last 2 years for existing entities)'
    ],
    documentsTa: [
      '3 ஆண்டு முன்னறிவிப்புகளுடன் வணிகத் திட்டம்',
      'வன்பொருள்/மென்பொருளின் தொழில்நுட்ப விவரக்குறிப்புகள்',
      'ஸ்டுடியோ இருப்பிடம் மற்றும் குத்தகை/உரிமை ஆவணங்கள்',
      'குழு அமைப்பு மற்றும் பணியமர்த்தல் திட்டம்',
      'நிதி அறிக்கைகள் (ஏற்கனவே உள்ள நிறுவனங்களுக்கு கடைசி 2 ஆண்டுகள்)'
    ],
    applicantsCount: 67,
    sanctionedCount: 23,
    icon: 'view_in_ar',
    color: 'var(--color-sector-xr)',
    featured: true,
    highlights: [
      'Up to ₹1 crore capital grant',
      'Covers 40% of setup costs',
      '5-year operational commitment',
      'Tax incentives for 5 years'
    ],
    highlightsTa: [
      'அதிகபட்சம் ₹1 கோடி மூலதன மானியம்',
      'அமைவு செலவுகளில் 40% உள்ளடக்கும்',
      '5 ஆண்டு செயல்பாட்டு உறுதி',
      '5 ஆண்டுகளுக்கு வரி சலுகைகள்'
    ],
    processingTime: '90 working days',
    processingTimeTa: '90 வேலை நாட்கள்',
    benefits: [
      'Up to ₹1 crore for studio setup',
      '40% reimbursement of approved costs',
      'Priority access to talent pool',
      'Showcase opportunities at international events'
    ],
    benefitsTa: [
      'ஸ்டுடியோ அமைப்புக்கு அதிகபட்சம் ₹1 கோடி',
      'அங்கீகரிக்கப்பட்ட செலவுகளில் 40% திருப்பிச் செலுத்துதல்',
      'திறமை குழாமத்திற்கு முன்னுரிமை அணுகல்',
      'சர்வதேச நிகழ்வுகளில் காட்சி வாய்ப்புகள்'
    ],
    applicationUrl: '/auth/register?scheme=scheme-003'
  },
  {
    id: 'scheme-004',
    name: 'International Market Access Subsidy',
    nameTa: 'சர்வதேச சந்தை அணுகல் மானியம்',
    slug: 'international-market-access-subsidy',
    description: 'Subsidy for participating in international AVGC trade events, conferences, and festivals — covering travel, booth, and marketing costs.',
    descriptionTa: 'சர்வதேச AVGC வர்த்தக நிகழ்வுகள், மாநாடுகள் மற்றும் திருவிழாக்களில் பங்கேற்பதற்கான மானியம் — பயணம், சாவடி மற்றும் சந்தைப்படுத்தல் செலவுகளை உள்ளடக்கியது.',
    department: 'Tamil Nadu Trade Promotion Organisation',
    departmentTa: 'தமிழ்நாடு வர்த்தக மேம்பாட்டு அமைப்பு',
    category: 'export',
    maxAmount: 500000,
    currency: 'INR',
    status: 'active',
    deadline: '2026-10-30',
    eligibility: 'Tamil Nadu-based AVGC companies with international business interests. Must be a registered Indian company with operations in Tamil Nadu for at least 1 year.',
    eligibilityTa: 'சர்வதேச வணிக ஆர்வங்களுடன் தமிழ்நாடு AVGC நிறுவனங்கள். குறைந்தபட்சம் 1 ஆண்டு தமிழ்நாட்டில் செயல்பாடுகளுடன் பதிவு செய்யப்பட்ட இந்திய நிறுவனமாக இருக்க வேண்டும்.',
    documents: [
      'Event registration confirmation',
      'Company profile and portfolio',
      'Export license (if applicable)',
      'Project proposal with ROI projections',
      'Travel and accommodation quotations'
    ],
    documentsTa: [
      'நிகழ்வு பதிவு உறுதிப்படுத்தல்',
      'நிறுவன சுயவிவரம் மற்றும் போர்ட்ஃபோலியோ',
      'ஏற்றுமதி உரிமம் (பொருந்தினால்)',
      'ROI முன்னறிவிப்புகளுடன் திட்ட முன்மொழிவு',
      'பயணம் மற்றும் தங்கும் மதிப்பீடுகள்'
    ],
    applicantsCount: 42,
    sanctionedCount: 31,
    icon: 'public',
    color: 'var(--color-sector-vfx)',
    featured: true,
    highlights: [
      'Up to ₹5 lakhs per event',
      'Covers 70% of travel and booth costs',
      'Up to 3 events per year',
      'Priority for first-time exporters'
    ],
    highlightsTa: [
      'ஒரு நிகழ்வுக்கு அதிகபட்சம் ₹5 லட்சம்',
      'பயணம் மற்றும் சாவடி செலவுகளில் 70% உள்ளடக்கும்',
      'ஆண்டுக்கு 3 நிகழ்வுகள் வரை',
      'முதல் முறை ஏற்றுமதியாளர்களுக்கு முன்னுரிமை'
    ],
    processingTime: '21 working days',
    processingTimeTa: '21 வேலை நாட்கள்',
    benefits: [
      'Reimbursement of approved travel costs',
      'Booth rental at major international events',
      'Marketing collateral design support',
      'Networking opportunities with global buyers'
    ],
    benefitsTa: [
      'அங்கீகரிக்கப்பட்ட பயணச் செலவுகளின் திருப்பிச் செலுத்துதல்',
      'முக்கிய சர்வதேச நிகழ்வுகளில் சாவடி வாடகை',
      'சந்தைப்படுத்தல் துணை பொருள் வடிவமைப்பு ஆதரவு',
      'உலகளாவிய வாங்குபவர்களுடன் நெட்வொர்க்கிங் வாய்ப்புகள்'
    ],
    applicationUrl: '/auth/register?scheme=scheme-004'
  },
  {
    id: 'scheme-005',
    name: 'Freelancer Registration & Support',
    nameTa: 'ஃப்ரீலான்ஸர் பதிவு & ஆதரவு',
    slug: 'freelancer-registration-support',
    description: 'Official registration for AVGC freelancers with access to project opportunities, skill development, and industry events.',
    descriptionTa: 'AVGC ஃப்ரீலான்ஸர்களுக்கான அதிகாரப்பூர்வ பதிவு, திட்ட வாய்ப்புகள், திறன் மேம்பாடு மற்றும் தொழில்துறை நிகழ்வுகளுக்கான அணுகலுடன்.',
    department: 'Tamil Nadu Creator Economy Mission',
    departmentTa: 'தமிழ்நாடு கிரியேட்டர் பொருளாதார இயக்கம்',
    category: 'freelancer',
    maxAmount: 50000,
    currency: 'INR',
    status: 'active',
    deadline: '2026-12-15',
    eligibility: 'Individual freelancers in animation, VFX, gaming, comics, or XR with portfolio of work. No company registration required — individuals only.',
    eligibilityTa: 'பணிகளின் போர்ட்ஃபோலியோவுடன் அனிமேஷன், VFX, கேமிங், காமிக்ஸ் அல்லது XR இல் தனிநபர் ஃப்ரீலான்ஸர்கள். நிறுவன பதிவு தேவையில்லை — தனிநபர்கள் மட்டும்.',
    documents: [
      'Aadhaar card',
      'PAN card',
      'Bank account details',
      'Portfolio (online link, 5+ sample works)',
      'Resume/CV'
    ],
    documentsTa: [
      'ஆதார் அட்டை',
      'PAN அட்டை',
      'வங்கி கணக்கு விவரங்கள்',
      'போர்ட்ஃபோலியோ (ஆன்லைன் இணைப்பு, 5+ மாதிரி பணிகள்)',
      'ரெஸ்யூமே/CV'
    ],
    applicantsCount: 5621,
    sanctionedCount: 4892,
    icon: 'person',
    color: 'var(--color-sector-comics)',
    featured: true,
    highlights: [
      'Free registration',
      'Profile visibility to 200+ companies',
      'Project opportunity notifications',
      'Skill development workshops'
    ],
    highlightsTa: [
      'இலவச பதிவு',
      '200+ நிறுவனங்களுக்கு சுயவிவர தெரிவுநிலை',
      'திட்ட வாய்ப்பு அறிவிப்புகள்',
      'திறன் மேம்பாட்டு பட்டறைகள்'
    ],
    processingTime: '14 working days',
    processingTimeTa: '14 வேலை நாட்கள்',
    benefits: [
      'Verified freelancer badge',
      'Priority access to project postings',
      'Annual meetup and awards',
      'Tax guidance for freelancers'
    ],
    benefitsTa: [
      'சரிபார்க்கப்பட்ட ஃப்ரீலான்ஸர் பேட்ஜ்',
      'திட்ட இடுகைகளுக்கு முன்னுரிமை அணுகல்',
      'ஆண்டு சந்திப்பு மற்றும் விருதுகள்',
      'ஃப்ரீலான்ஸர்களுக்கான வரி வழிகாட்டுதல்'
    ],
    applicationUrl: '/auth/register?scheme=scheme-005'
  },
  {
    id: 'scheme-006',
    name: 'AVGC Scholarship Program',
    nameTa: 'AVGC கல்வித் திட்டம்',
    slug: 'avgc-scholarship-program',
    description: 'Merit and need-based scholarships for students pursuing AVGC-XR related courses at recognized institutions.',
    descriptionTa: 'அங்கீகரிக்கப்பட்ட நிறுவனங்களில் AVGC-XR தொடர்பான படிப்புகளைத் தொடரும் மாணவர்களுக்கான திறமை மற்றும் தேவை அடிப்படையிலான கல்வித் திட்டங்கள்.',
    department: 'Higher Education Department, Government of Tamil Nadu',
    departmentTa: 'உயர்கல்வித் துறை, தமிழ்நாடு அரசு',
    category: 'scholarship',
    maxAmount: 100000,
    currency: 'INR',
    status: 'active',
    deadline: '2026-09-30',
    eligibility: 'Students enrolled in AVGC-XR related undergraduate or postgraduate programs at recognized institutions. Family income must be below ₹8 lakhs per annum.',
    eligibilityTa: 'அங்கீகரிக்கப்பட்ட நிறுவனங்களில் AVGC-XR தொடர்பான இளங்கலை அல்லது முதுகலை படிப்புகளில் பதிவுசெய்யப்பட்ட மாணவர்கள். குடும்ப வருமானம் ஆண்டுக்கு ₹8 லட்சத்திற்குக் கீழ் இருக்க வேண்டும்.',
    documents: [
      'Aadhaar card',
      'Bonafide certificate from institution',
      'Previous year mark sheets',
      'Income certificate',
      'Bank account details'
    ],
    documentsTa: [
      'ஆதார் அட்டை',
      'நிறுவனத்தின் போனஃபைடு சான்றிதழ்',
      'முந்தைய ஆண்டு மதிப்பெண் தாள்கள்',
      'வருமான சான்றிதழ்',
      'வங்கி கணக்கு விவரங்கள்'
    ],
    applicantsCount: 892,
    sanctionedCount: 567,
    icon: 'menu_book',
    color: 'var(--color-primary)',
    featured: false,
    highlights: [
      'Up to ₹1 lakh per year',
      'Covers tuition and equipment',
      'Renewable annually based on performance',
      'Open to all 5 AVGC verticals'
    ],
    highlightsTa: [
      'ஆண்டுக்கு அதிகபட்சம் ₹1 லட்சம்',
      'கட்டணம் மற்றும் உபகரணங்களை உள்ளடக்கும்',
      'செயல்திறன் அடிப்படையில் ஆண்டுதோறும் புதுப்பிக்கத்தக்கது',
      'அனைத்து 5 AVGC கிடைமட்டங்களுக்கும் திறந்துள்ளது'
    ],
    processingTime: '60 working days',
    processingTimeTa: '60 வேலை நாட்கள்',
    benefits: [
      'Tuition fee reimbursement',
      'Equipment allowance (₹25,000)',
      'Book and software grants',
      'Mentorship from industry professionals'
    ],
    benefitsTa: [
      'கட்டணம் திருப்பிச் செலுத்துதல்',
      'உபகரண உதவித்தொகை (₹25,000)',
      'புத்தக மற்றும் மென்பொருள் மானியங்கள்',
      'தொழில்துறை நிபுணர்களிடமிருந்து வழிகாட்டல்'
    ],
    applicationUrl: '/auth/register?scheme=scheme-006'
  }
];

export const SECTOR_CATEGORIES = [
  { key: 'ANIMATION', icon: 'movie', labelEn: 'Animation', labelTa: 'அனிமேஷன்', color: 'var(--color-sector-animation)' },
  { key: 'VFX', icon: 'auto_fix_high', labelEn: 'VFX', labelTa: 'காட்சி விளைவுகள்', color: 'var(--color-sector-vfx)' },
  { key: 'GAMING', icon: 'sports_esports', labelEn: 'Gaming', labelTa: 'கேமிங்', color: 'var(--color-sector-gaming)' },
  { key: 'COMICS', icon: 'auto_stories', labelEn: 'Comics', labelTa: 'காமிக்ஸ்', color: 'var(--color-sector-comics)' },
  { key: 'XR', icon: 'view_in_ar', labelEn: 'Extended Reality', labelTa: 'நீட்டிக்கப்பட்ட யதார்த்தம்', color: 'var(--color-sector-xr)' }
];

export const HOMEPAGE_STATS = {
  applications: 7821,
  beneficiaries: 12453,
  studios: 247,
  freelancers: 5621
};

export const HOMEPAGE_NEWS = [
  {
    id: 'news-001',
    titleEn: 'Tamil Nadu AVGC-XR Policy 2024 Launched',
    titleTa: 'தமிழ்நாடு AVGC-XR கொள்கை 2024 அறிமுகம்',
    excerptEn: 'Government of Tamil Nadu launches comprehensive policy to boost the AVGC-XR sector with ₹500 crore investment over 5 years.',
    excerptTa: 'தமிழ்நாடு அரசு 5 ஆண்டுகளில் ₹500 கோடி முதலீட்டுடன் AVGC-XR துறையை மேம்படுத்த விரிவான கொள்கையை அறிமுகப்படுத்துகிறது.',
    date: '2026-06-15',
    category: 'policy',
    icon: 'campaign'
  },
  {
    id: 'news-002',
    titleEn: 'ELCOT Partners with 5 International Studios for Talent Exchange',
    titleTa: 'திறமை பரிமாற்றத்திற்காக ELCOT 5 சர்வதேச ஸ்டுடியோக்களுடன் கூட்டாண்மை',
    excerptEn: 'Memorandum of Understanding signed with studios from South Korea, Japan, Singapore, Canada, and the UK for talent exchange and co-production.',
    excerptTa: 'திறமை பரிமாற்றம் மற்றும் கூட்டு தயாரிப்புக்காக தென் கொரியா, ஜப்பான், சிங்கப்பூர், கனடா மற்றும் UK ஆகிய நாடுகளின் ஸ்டுடியோக்களுடன் புரிந்துணர்வு ஒப்பந்தம் கையெழுத்தானது.',
    date: '2026-06-08',
    category: 'partnership',
    icon: 'handshake'
  },
  {
    id: 'news-003',
    titleEn: '₹100 Crore Skill Development Fund Announced for AVGC-XR',
    titleTa: 'AVGC-XR க்கு ₹100 கோடி திறன் மேம்பாட்டு நிதி அறிவிப்பு',
    excerptEn: 'Major funding boost for AVGC-XR training programs across 25 colleges in Tamil Nadu. Trainings begin July 2026.',
    excerptTa: 'தமிழ்நாட்டில் 25 கல்லூரிகளில் AVGC-XR பயிற்சி திட்டங்களுக்கு முக்கிய நிதி உயர்வு. பயிற்சிகள் ஜூலை 2026 இல் தொடங்கும்.',
    date: '2026-06-01',
    category: 'funding',
    icon: 'savings'
  }
];
